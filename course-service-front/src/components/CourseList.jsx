import { useEffect, useState } from "react";
import { getAllCourses, deleteCourse, addCourse } from "../api/courseApi";
import SearchBar from "./SearchBar";
import axios from "axios";

const BASE_URL = "http://localhost:8083/courses";

export default function CourseList() {
    const [courses, setCourses] = useState([]);
    const [newTitle, setNewTitle] = useState("");
    const [newDesc, setNewDesc]   = useState("");
    const [editingId, setEditingId] = useState(null);
    const [editTitle, setEditTitle] = useState("");
    const [editDesc,  setEditDesc]  = useState("");
    const [loading, setLoading] = useState(true);

    const fetchCourses = async () => {
        setLoading(true);
        const res = await getAllCourses();
        setCourses(res.data);
        setLoading(false);
    };

    useEffect(() => { fetchCourses(); }, []);

    const handleDelete = async (id) => {
        await deleteCourse(id);
        fetchCourses();
    };

    const handleAdd = async () => {
        if (!newTitle.trim()) return;
        await addCourse({ title: newTitle, description: newDesc });
        setNewTitle(""); setNewDesc("");
        fetchCourses();
    };

    const handleEditOpen = (c) => {
        setEditingId(c.id); setEditTitle(c.title); setEditDesc(c.description);
    };

    const handleEditSave = async (id) => {
        await axios.put(`${BASE_URL}/${id}`, { title: editTitle, description: editDesc });
        setEditingId(null);
        fetchCourses();
    };

    return (
        <div style={{ minHeight: "100vh", background: "var(--bg)" }}>

            {/* ── Header ── */}
            <header style={{
                background: "linear-gradient(135deg, var(--primary) 0%, #1e40af 100%)",
                padding: "32px 0 48px", marginBottom: "-24px"
            }}>
                <div style={{ maxWidth: "860px", margin: "0 auto", padding: "0 24px" }}>
                    <div style={{ display: "flex", alignItems: "center", gap: "12px", marginBottom: "6px" }}>
                        <span style={{ fontSize: "28px" }}>📚</span>
                        <h1 style={{
                            fontFamily: "'Playfair Display', serif",
                            fontSize: "clamp(22px, 4vw, 32px)",
                            color: "#fff", fontWeight: 600, letterSpacing: "-.5px"
                        }}>SmartCampus</h1>
                    </div>
                    <p style={{ color: "rgba(255,255,255,.75)", fontSize: "15px", marginLeft: "40px" }}>
                        Gérez vos cours en toute simplicité
                    </p>
                </div>
            </header>

            {/* ── Main ── */}
            <main style={{ maxWidth: "860px", margin: "0 auto", padding: "0 24px 80px" }}>

                {/* ── Add card ── */}
                <div style={{
                    background: "var(--card)", borderRadius: "var(--radius-lg)",
                    boxShadow: "var(--shadow-lg)", padding: "24px",
                    marginBottom: "24px", border: "1px solid var(--border)"
                }}>
                    <h2 style={{ fontSize: "15px", fontWeight: 600, color: "var(--text-muted)", marginBottom: "16px", textTransform: "uppercase", letterSpacing: ".05em" }}>
                        ➕ Nouveau cours
                    </h2>
                    <div style={{ display: "flex", gap: "12px", flexWrap: "wrap" }}>
                        <input
                            value={newTitle} onChange={e => setNewTitle(e.target.value)}
                            placeholder="Titre du cours"
                            style={inputStyle}
                            onFocus={focusStyle} onBlur={blurStyle}
                        />
                        <input
                            value={newDesc} onChange={e => setNewDesc(e.target.value)}
                            placeholder="Description (optionnelle)"
                            style={{ ...inputStyle, flex: 2 }}
                            onFocus={focusStyle} onBlur={blurStyle}
                        />
                        <button onClick={handleAdd} style={btnPrimary}
                                onMouseEnter={e => e.target.style.background = "var(--primary-dark)"}
                                onMouseLeave={e => e.target.style.background = "var(--primary)"}
                        >
                            Ajouter
                        </button>
                    </div>
                </div>

                {/* ── Search ── */}
                <SearchBar onResults={setCourses} />

                {/* ── Stats ── */}
                <div style={{ display: "flex", gap: "12px", marginBottom: "20px" }}>
                    <div style={statCard}>
                        <span style={{ fontSize: "24px", fontWeight: 700, color: "var(--primary)" }}>{courses.length}</span>
                        <span style={{ fontSize: "13px", color: "var(--text-muted)" }}>cours disponibles</span>
                    </div>
                </div>

                {/* ── Course list ── */}
                {loading ? (
                    <div style={{ textAlign: "center", padding: "60px", color: "var(--text-muted)" }}>
                        <span style={{ fontSize: "32px" }}>⏳</span>
                        <p style={{ marginTop: "12px" }}>Chargement...</p>
                    </div>
                ) : courses.length === 0 ? (
                    <div style={{ textAlign: "center", padding: "60px", color: "var(--text-muted)" }}>
                        <span style={{ fontSize: "48px" }}>📭</span>
                        <p style={{ marginTop: "12px", fontSize: "16px" }}>Aucun cours trouvé.</p>
                    </div>
                ) : (
                    <div style={{ display: "flex", flexDirection: "column", gap: "12px" }}>
                        {courses.map((c, i) => (
                            <div key={c.id} style={{
                                background: "var(--card)", borderRadius: "var(--radius)",
                                boxShadow: "var(--shadow-sm)", border: "1px solid var(--border)",
                                padding: "20px 24px", transition: "box-shadow .2s, transform .2s",
                                animation: `fadeIn .3s ease ${i * 0.05}s both`
                            }}
                                 onMouseEnter={e => { e.currentTarget.style.boxShadow = "var(--shadow-md)"; e.currentTarget.style.transform = "translateY(-1px)"; }}
                                 onMouseLeave={e => { e.currentTarget.style.boxShadow = "var(--shadow-sm)"; e.currentTarget.style.transform = "none"; }}
                            >
                                {editingId === c.id ? (
                                    <div style={{ display: "flex", gap: "10px", alignItems: "center", flexWrap: "wrap" }}>
                                        <input value={editTitle} onChange={e => setEditTitle(e.target.value)}
                                               style={{ ...inputStyle, flex: 1 }} onFocus={focusStyle} onBlur={blurStyle} />
                                        <input value={editDesc} onChange={e => setEditDesc(e.target.value)}
                                               style={{ ...inputStyle, flex: 2 }} onFocus={focusStyle} onBlur={blurStyle} />
                                        <button onClick={() => handleEditSave(c.id)} style={btnSuccess}
                                                onMouseEnter={e => e.target.style.background = "#059669"}
                                                onMouseLeave={e => e.target.style.background = "var(--success)"}
                                        >💾 Sauver</button>
                                        <button onClick={() => setEditingId(null)} style={btnGray}
                                                onMouseEnter={e => e.target.style.background = "#94a3b8"}
                                                onMouseLeave={e => e.target.style.background = "#cbd5e1"}
                                        >✕</button>
                                    </div>
                                ) : (
                                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", gap: "16px" }}>
                                        <div style={{ display: "flex", alignItems: "center", gap: "16px" }}>
                                            <div style={{
                                                width: "44px", height: "44px", borderRadius: "10px",
                                                background: "var(--primary-light)", display: "flex",
                                                alignItems: "center", justifyContent: "center",
                                                fontSize: "20px", flexShrink: 0
                                            }}>📖</div>
                                            <div>
                                                <p style={{ fontWeight: 600, fontSize: "16px", color: "var(--text)" }}>{c.title}</p>
                                                {c.description && (
                                                    <p style={{ fontSize: "13px", color: "var(--text-muted)", marginTop: "2px" }}>{c.description}</p>
                                                )}
                                            </div>
                                        </div>
                                        <div style={{ display: "flex", gap: "8px", flexShrink: 0 }}>
                                            <button onClick={() => handleEditOpen(c)} style={btnEdit}
                                                    onMouseEnter={e => e.target.style.background = "#fef3c7"}
                                                    onMouseLeave={e => e.target.style.background = "transparent"}
                                            >✏️ Modifier</button>
                                            <button onClick={() => handleDelete(c.id)} style={btnDanger}
                                                    onMouseEnter={e => e.target.style.background = "#fee2e2"}
                                                    onMouseLeave={e => e.target.style.background = "transparent"}
                                            >🗑️</button>
                                        </div>
                                    </div>
                                )}
                            </div>
                        ))}
                    </div>
                )}
            </main>

            <style>{`
        @keyframes fadeIn {
          from { opacity: 0; transform: translateY(8px); }
          to   { opacity: 1; transform: translateY(0); }
        }
      `}</style>
        </div>
    );
}

/* ── Shared styles ── */
const inputStyle = {
    flex: 1, padding: "10px 14px", fontSize: "14px",
    fontFamily: "'DM Sans', sans-serif",
    border: "2px solid var(--border)", borderRadius: "8px",
    background: "var(--bg)", color: "var(--text)",
    outline: "none", transition: "border-color .2s, box-shadow .2s",
    minWidth: "120px"
};

const focusStyle = e => {
    e.target.style.borderColor = "var(--primary)";
    e.target.style.boxShadow = "0 0 0 4px rgba(37,99,235,.1)";
};
const blurStyle = e => {
    e.target.style.borderColor = "var(--border)";
    e.target.style.boxShadow = "none";
};

const baseBtn = {
    padding: "10px 18px", fontSize: "14px", fontWeight: 500,
    fontFamily: "'DM Sans', sans-serif",
    border: "none", borderRadius: "8px", cursor: "pointer",
    transition: "background .2s, transform .1s", whiteSpace: "nowrap"
};

const btnPrimary = { ...baseBtn, background: "var(--primary)", color: "#fff" };
const btnSuccess = { ...baseBtn, background: "var(--success)", color: "#fff" };
const btnGray    = { ...baseBtn, background: "#cbd5e1", color: "var(--text)" };
const btnEdit    = { ...baseBtn, background: "transparent", color: "var(--accent)", border: "1px solid #fde68a" };
const btnDanger  = { ...baseBtn, background: "transparent", color: "var(--danger)", border: "1px solid #fecaca" };

const statCard = {
    background: "var(--card)", border: "1px solid var(--border)",
    borderRadius: "var(--radius)", padding: "12px 20px",
    display: "flex", alignItems: "center", gap: "10px",
    boxShadow: "var(--shadow-sm)"
};