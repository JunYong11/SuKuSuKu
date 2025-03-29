package com.web.sukusuku.dto;

public class CompletionStatusRequest {
    private Long calendarId;
    private boolean completed;

    // Getter & Setter
    public Long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
