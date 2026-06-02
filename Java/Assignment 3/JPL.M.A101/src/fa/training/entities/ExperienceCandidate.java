package fa.training.entities;

import java.io.Serial;
import java.util.*;
public class ExperienceCandidate extends Candidate {
    @Serial
    private static final long serialVersionUID = 1L;
    private int yearExperience;
    private String professionalSkill;

    public ExperienceCandidate(){
        super();
    }

    public ExperienceCandidate(String firstName, String lastName, Date birthDate, String address, String phone, String email, int yearExperience, String professionalSkill){
        super(firstName, lastName, birthDate, address, phone, email);
        this.yearExperience = yearExperience;
        this.professionalSkill = professionalSkill;
    }

    public int getYearExperience() {
        return yearExperience;
    }

    public void setYearExperience(int yearExperience) {
        this.yearExperience = yearExperience;
    }

    public String getProfessionalSkill() {
        return professionalSkill;
    }

    public void setProfessionalSkill(String professionalSkill) {
        this.professionalSkill = professionalSkill;
    }
}
