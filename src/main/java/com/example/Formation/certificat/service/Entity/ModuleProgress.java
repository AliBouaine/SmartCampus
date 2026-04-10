package com.example.Formation.certificat.service.Entity;

import java.time.LocalDateTime;

public class ModuleProgress {
    private String moduleId;
    private boolean complete;
    private LocalDateTime dateCompletion;

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public LocalDateTime getDateCompletion() {
        return dateCompletion;
    }

    public void setDateCompletion(LocalDateTime dateCompletion) {
        this.dateCompletion = dateCompletion;
    }

    public ModuleProgress(String moduleId, boolean complete, LocalDateTime dateCompletion) {
        this.moduleId = moduleId;
        this.complete = complete;
        this.dateCompletion = dateCompletion;
    }

    public ModuleProgress() {
    }
}
