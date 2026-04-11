import { useEffect, useState } from "react";
import { getAllCourses, deleteCourse, addCourse } from "../api/courseApi";
import SearchBar from "./SearchBar";
import axios from "axios";

const BASE_URL = "http://localhost:8083/courses";

export default function CourseList() {
    const [courses, setCourses] = useState([]);
    const [newTitle, setNewTitle] = useState("");
    const [newDesc, setNewDesc] = useState("");

    // ✅ état pour l'édition
    const [editingId, setEditingId] = useState(null);
    const [editTitle, setEditTitle] = useState("");
    const [editDesc, setEditDesc] = useState("");

    const fetchCourses = async () => {
        const res = await getAllCourses();
        setCourses(res.data);
    };

    useEffect(() => { fetchCourses(); }, []);

    const handleDelete = async (id) => {
        await deleteCourse(id);
        fetchCourses();
    };

    const handleAdd = async () => {
        if (!newTitle.trim()) return;
        await addCourse({ title: newTitle, description: newDesc });
        setNewTitle("");
        setNewDesc("");
        fetchCourses();
    };

    // ✅ ouvrir le mode édition
    const handleEditOpen = (course) => {
        setEditingId(course.id);
        setEditTitle(course.title);
        setEditDesc(course.description);
    };

    // ✅ sauvegarder la modification
    const handleEditSave = async (id) => {
        await axios.put(`${BASE_URL}/${id}`, {
            title: editTitle,
            description: editDesc
        });
        setEditingId(null);
        fetchCourses();
    };

    return (
        <div style={{ padding: "20px", maxWidth: "800px", margin: "0 auto" }}>
            <h1>📚 SmartCampus — Cours</h1>

            <SearchBar onResults={setCourses} />

            {/* Formulaire ajout */}
            <div style={{ marginBottom: "20px", display: "flex", gap: "10px" }}>
                <input
                    value={newTitle}
                    onChange={(e) => setNewTitle(e.target.value)}
                    placeholder="Titre du cours"
                    style={{ flex: 1, padding: "8px", borderRadius: "6px", border: "1px solid #ccc" }}
                />
                <input
                    value={newDesc}
                    onChange={(e) => setNewDesc(e.target.value)}
                    placeholder="Description"
                    style={{ flex: 2, padding: "8px", borderRadius: "6px", border: "1px solid #ccc" }}
                />
                <button
                    onClick={handleAdd}
                    style={{ padding: "8px 16px", backgroundColor: "#4CAF50", color: "white", border: "none", borderRadius: "6px", cursor: "pointer" }}
                >
                    ➕ Ajouter
                </button>
            </div>

            {/* Liste des cours */}
            {courses.length === 0 ? (
                <p>Aucun cours trouvé.</p>
            ) : (
                courses.map((c) => (
                    <div key={c.id} style={{
                        padding: "15px", marginBottom: "10px",
                        border: "1px solid #ddd", borderRadius: "8px"
                    }}>
                        {editingId === c.id ? (
                            // ✅ Mode édition
                            <div style={{ display: "flex", gap: "10px", alignItems: "center" }}>
                                <input
                                    value={editTitle}
                                    onChange={(e) => setEditTitle(e.target.value)}
                                    style={{ flex: 1, padding: "6px", borderRadius: "6px", border: "1px solid #ccc" }}
                                />
                                <input
                                    value={editDesc}
                                    onChange={(e) => setEditDesc(e.target.value)}
                                    style={{ flex: 2, padding: "6px", borderRadius: "6px", border: "1px solid #ccc" }}
                                />
                                <button
                                    onClick={() => handleEditSave(c.id)}
                                    style={{ padding: "6px 12px", backgroundColor: "#1976d2", color: "white", border: "none", borderRadius: "6px", cursor: "pointer" }}
                                >
                                    💾 Sauver
                                </button>
                                <button
                                    onClick={() => setEditingId(null)}
                                    style={{ padding: "6px 12px", backgroundColor: "#aaa", color: "white", border: "none", borderRadius: "6px", cursor: "pointer" }}
                                >
                                    ✕
                                </button>
                            </div>
                        ) : (
                            // ✅ Mode affichage
                            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                                <div>
                                    <strong>{c.title}</strong>
                                    <p style={{ margin: 0, color: "#666" }}>{c.description}</p>
                                </div>
                                <div style={{ display: "flex", gap: "8px" }}>
                                    <button
                                        onClick={() => handleEditOpen(c)}
                                        style={{ padding: "6px 12px", backgroundColor: "#ff9800", color: "white", border: "none", borderRadius: "6px", cursor: "pointer" }}
                                    >
                                        ✏️ Modifier
                                    </button>
                                    <button
                                        onClick={() => handleDelete(c.id)}
                                        style={{ padding: "6px 12px", backgroundColor: "#f44336", color: "white", border: "none", borderRadius: "6px", cursor: "pointer" }}
                                    >
                                        🗑️ Supprimer
                                    </button>
                                </div>
                            </div>
                        )}
                    </div>
                ))
            )}
        </div>
    );
}