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
        <div style={{ marginBottom: "20px" }}>
    <input
        type="text"
    value={query}
    onChange={handleSearch}
    placeholder="🔍 Rechercher un cours par titre..."
    style={{
        width: "100%",
            padding: "10px",
            fontSize: "16px",
            borderRadius: "8px",
            border: "1px solid #ccc"
    }}
    />
    </div>
);
}

