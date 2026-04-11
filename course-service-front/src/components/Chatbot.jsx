import { useState, useRef, useEffect } from "react";
import { sendChatMessage } from "../api/courseApi";

export default function Chatbot() {
    const [open, setOpen]       = useState(false);
    const [messages, setMessages] = useState([
        { from: "bot", text: "Bonjour ! Je suis l'assistant SmartCampus 👋\nPosez-moi vos questions sur les cours." }
    ]);
    const [input, setInput]     = useState("");
    const [typing, setTyping]   = useState(false);
    const bottomRef             = useRef(null);

    useEffect(() => {
        bottomRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages, typing]);

    const handleSend = async () => {
        if (!input.trim()) return;
        const userMsg = { from: "user", text: input };
        setMessages(prev => [...prev, userMsg]);
        setInput("");
        setTyping(true);
        try {
            const res = await sendChatMessage(input);
            setTimeout(() => {
                setTyping(false);
                setMessages(prev => [...prev, { from: "bot", text: res.data.response }]);
            }, 600);
        } catch {
            setTyping(false);
            setMessages(prev => [...prev, { from: "bot", text: "❌ Erreur de connexion au serveur." }]);
        }
    };

    const handleKey = e => { if (e.key === "Enter" && !e.shiftKey) handleSend(); };

    return (
        <div style={{ position: "fixed", bottom: "24px", right: "24px", zIndex: 1000, fontFamily: "'DM Sans', sans-serif" }}>

            {/* ── Chat window ── */}
            {open && (
                <div style={{
                    width: "340px", height: "460px",
                    background: "#fff", borderRadius: "20px",
                    boxShadow: "0 20px 60px rgba(0,0,0,.18), 0 4px 20px rgba(37,99,235,.12)",
                    display: "flex", flexDirection: "column", overflow: "hidden",
                    marginBottom: "12px", border: "1px solid var(--border)",
                    animation: "slideUp .25s ease"
                }}>

                    {/* Header */}
                    <div style={{
                        background: "linear-gradient(135deg, var(--primary), #1e40af)",
                        padding: "16px 20px", display: "flex", alignItems: "center", gap: "12px"
                    }}>
                        <div style={{
                            width: "38px", height: "38px", borderRadius: "50%",
                            background: "rgba(255,255,255,.2)", display: "flex",
                            alignItems: "center", justifyContent: "center", fontSize: "18px"
                        }}>🤖</div>
                        <div>
                            <p style={{ color: "#fff", fontWeight: 600, fontSize: "15px", margin: 0 }}>Assistant SmartCampus</p>
                            <p style={{ color: "rgba(255,255,255,.7)", fontSize: "12px", margin: 0 }}>● En ligne</p>
                        </div>
                        <button onClick={() => setOpen(false)} style={{
                            marginLeft: "auto", background: "rgba(255,255,255,.15)",
                            border: "none", borderRadius: "8px", color: "#fff",
                            width: "28px", height: "28px", cursor: "pointer", fontSize: "14px"
                        }}>✕</button>
                    </div>

                    {/* Messages */}
                    <div style={{ flex: 1, overflowY: "auto", padding: "16px", display: "flex", flexDirection: "column", gap: "10px", background: "#f8fafc" }}>
                        {messages.map((msg, i) => (
                            <div key={i} style={{ display: "flex", justifyContent: msg.from === "user" ? "flex-end" : "flex-start" }}>
                                {msg.from === "bot" && (
                                    <div style={{
                                        width: "28px", height: "28px", borderRadius: "50%",
                                        background: "var(--primary-light)", display: "flex",
                                        alignItems: "center", justifyContent: "center",
                                        fontSize: "14px", marginRight: "8px", flexShrink: 0, alignSelf: "flex-end"
                                    }}>🤖</div>
                                )}
                                <div style={{
                                    maxWidth: "75%", padding: "10px 14px",
                                    borderRadius: msg.from === "user" ? "16px 16px 4px 16px" : "16px 16px 16px 4px",
                                    background: msg.from === "user" ? "var(--primary)" : "#fff",
                                    color: msg.from === "user" ? "#fff" : "var(--text)",
                                    fontSize: "13.5px", lineHeight: "1.5",
                                    boxShadow: "var(--shadow-sm)",
                                    border: msg.from === "bot" ? "1px solid var(--border)" : "none",
                                    whiteSpace: "pre-line"
                                }}>
                                    {msg.text}
                                </div>
                            </div>
                        ))}

                        {/* Typing indicator */}
                        {typing && (
                            <div style={{ display: "flex", alignItems: "flex-end", gap: "8px" }}>
                                <div style={{
                                    width: "28px", height: "28px", borderRadius: "50%",
                                    background: "var(--primary-light)", display: "flex",
                                    alignItems: "center", justifyContent: "center", fontSize: "14px"
                                }}>🤖</div>
                                <div style={{
                                    background: "#fff", border: "1px solid var(--border)",
                                    borderRadius: "16px 16px 16px 4px", padding: "12px 16px",
                                    display: "flex", gap: "4px", alignItems: "center"
                                }}>
                                    {[0,1,2].map(n => (
                                        <div key={n} style={{
                                            width: "6px", height: "6px", borderRadius: "50%",
                                            background: "var(--text-muted)",
                                            animation: `bounce .8s ease ${n * 0.15}s infinite`
                                        }} />
                                    ))}
                                </div>
                            </div>
                        )}
                        <div ref={bottomRef} />
                    </div>

                    {/* Input */}
                    <div style={{
                        padding: "12px 16px", borderTop: "1px solid var(--border)",
                        display: "flex", gap: "8px", background: "#fff"
                    }}>
                        <input
                            value={input} onChange={e => setInput(e.target.value)} onKeyDown={handleKey}
                            placeholder="Votre message..."
                            style={{
                                flex: 1, padding: "10px 14px", fontSize: "13.5px",
                                fontFamily: "'DM Sans', sans-serif",
                                border: "2px solid var(--border)", borderRadius: "10px",
                                outline: "none", color: "var(--text)", background: "var(--bg)"
                            }}
                            onFocus={e => e.target.style.borderColor = "var(--primary)"}
                            onBlur={e => e.target.style.borderColor = "var(--border)"}
                        />
                        <button onClick={handleSend} style={{
                            width: "40px", height: "40px", borderRadius: "10px",
                            background: "var(--primary)", color: "#fff",
                            border: "none", cursor: "pointer", fontSize: "16px",
                            display: "flex", alignItems: "center", justifyContent: "center",
                            transition: "background .2s"
                        }}
                                onMouseEnter={e => e.target.style.background = "var(--primary-dark)"}
                                onMouseLeave={e => e.target.style.background = "var(--primary)"}
                        >➤</button>
                    </div>
                </div>
            )}

            {/* ── Toggle button ── */}
            <div style={{ display: "flex", justifyContent: "flex-end" }}>
                <button onClick={() => setOpen(!open)} style={{
                    width: "58px", height: "58px", borderRadius: "50%",
                    background: open ? "#64748b" : "linear-gradient(135deg, var(--primary), #1e40af)",
                    color: "#fff", border: "none", cursor: "pointer", fontSize: "22px",
                    boxShadow: "0 4px 20px rgba(37,99,235,.4)",
                    display: "flex", alignItems: "center", justifyContent: "center",
                    transition: "transform .2s, background .2s"
                }}
                        onMouseEnter={e => e.currentTarget.style.transform = "scale(1.08)"}
                        onMouseLeave={e => e.currentTarget.style.transform = "scale(1)"}
                >
                    {open ? "✕" : "💬"}
                </button>
            </div>

            <style>{`
        @keyframes slideUp {
          from { opacity: 0; transform: translateY(16px) scale(.97); }
          to   { opacity: 1; transform: translateY(0) scale(1); }
        }
        @keyframes bounce {
          0%, 60%, 100% { transform: translateY(0); }
          30%           { transform: translateY(-5px); }
        }
      `}</style>
        </div>
    );
}