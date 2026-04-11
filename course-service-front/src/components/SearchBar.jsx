import { useState } from "react";
import { searchCourses, getAllCourses } from "../api/courseApi";

export default function SearchBar({ onResults }) {
    const [query, setQuery] = useState("");

    const handleSearch = async (e) => {
        const value = e.target.value;
        setQuery(value);
        if (value.trim() === "") {
            const res = await getAllCourses();
            onResults(res.data);
        } else {
            const res = await searchCourses(value);
            onResults(res.data);
        }
    };

    return (
        <div style={{ position: "relative", marginBottom: "24px" }}>
      <span style={{
          position: "absolute", left: "16px", top: "50%",
          transform: "translateY(-50%)", fontSize: "18px", pointerEvents: "none"
      }}>🔍</span>
            <input
                type="text"
                value={query}
                onChange={handleSearch}
                placeholder="Rechercher un cours par titre..."
                style={{
                    width: "100%", padding: "14px 16px 14px 48px",
                    fontSize: "15px", fontFamily: "'DM Sans', sans-serif",
                    border: "2px solid var(--border)", borderRadius: "var(--radius)",
                    background: "var(--card)", color: "var(--text)",
                    outline: "none", transition: "border-color .2s, box-shadow .2s",
                    boxShadow: "var(--shadow-sm)"
                }}
                onFocus={e => { e.target.style.borderColor = "var(--primary)"; e.target.style.boxShadow = "0 0 0 4px rgba(37,99,235,.1)"; }}
                onBlur={e => { e.target.style.borderColor = "var(--border)"; e.target.style.boxShadow = "var(--shadow-sm)"; }}
            />
        </div>
    );
}