import { useState } from "react";
import { sendChatMessage } from "../api/courseApi";

export default function Chatbot() {
    const [open, setOpen] = useState(false);
    const [messages, setMessages] = useState([
        { from: "bot", text: "Bonjour ! Je suis l'assistant SmartCampus. Comment puis-je vous aider ?" }
    ]);
    const [input, setInput] = useState("");

    const handleSend = async () => {
        if (!input.trim()) return;
        const userMsg = { from: "user", text: input };
        setMessages((prev) => [...prev, userMsg]);
        setInput("");

        try {
            const res = await sendChatMessage(input);
            const botMsg = { from: "bot", text: res.data.response };
            setMessages((prev) => [...prev, botMsg]);
        } catch {
            setMessages((prev) => [...prev, { from: "bot", text: "Erreur de connexion au serveur." }]);
        }
    };

    const handleKey = (e) => {
        if (e.key === "Enter") handleSend();
    };

    return (
        <div style={{ position: "fixed", bottom: "20px", right: "20px", zIndex: 1000 }}>
    {open && (
        <div style={{
        width: "320px", height: "420px", backgroundColor: "white",
            borderRadius: "12px", boxShadow: "0 4px 20px rgba(0,0,0,0.2)",
            display: "flex", flexDirection: "column", overflow: "hidden",
            marginBottom: "10px"
    }}>
        <div style={{ backgroundColor: "#1976d2", color: "white", padding: "12px 16px", fontWeight: "bold" }}>
    🤖 Assistant SmartCampus
    </div>
    <div style={{ flex: 1, overflowY: "auto", padding: "12px", display: "flex", flexDirection: "column", gap: "8px" }}>
        {messages.map((msg, i) => (
            <div key={i} style={{
            alignSelf: msg.from === "user" ? "flex-end" : "flex-start",
                backgroundColor: msg.from === "user" ? "#1976d2" : "#f0f0f0",
                color: msg.from === "user" ? "white" : "black",
                padding: "8px 12px", borderRadius: "12px", maxWidth: "80%", fontSize: "14px"
        }}>
            {msg.text}
            </div>
        ))}
        </div>
        <div style={{ display: "flex", borderTop: "1px solid #eee", padding: "8px" }}>
        <input
            value={input}
        onChange={(e) => setInput(e.target.value)}
        onKeyDown={handleKey}
        placeholder="Votre message..."
        style={{ flex: 1, padding: "8px", border: "1px solid #ccc", borderRadius: "6px", fontSize: "14px" }}
        />
        <button
        onClick={handleSend}
        style={{ marginLeft: "8px", padding: "8px 12px", backgroundColor: "#1976d2", color: "white", border: "none", borderRadius: "6px", cursor: "pointer" }}
    >
    ➤
                        </button>
                        </div>
                        </div>
    )}
    <button
        onClick={() => setOpen(!open)}
    style={{
        width: "56px", height: "56px", borderRadius: "50%",
            backgroundColor: "#1976d2", color: "white", fontSize: "24px",
            border: "none", cursor: "pointer", boxShadow: "0 2px 10px rgba(0,0,0,0.3)",
            display: "flex", alignItems: "center", justifyContent: "center"
    }}
>
    {open ? "✕" : "💬"}
    </button>
    </div>
);
}

