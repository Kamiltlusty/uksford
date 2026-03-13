package pl.uksford.api.entity;

public enum AcademicDegree {
    MGR("mgr."),
    DOK("dok."),
    DOK_HAB("dok. hab."),
    PROF_UCZELNI("prof. uczelni"),
    PROF("prof."),
    MGR_INZ("mgr. inż"),
    DOK_INZ("dok. inż"),
    DOK_HAB_INZ("dok. hab. inż");

    private final String label;

    AcademicDegree(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
