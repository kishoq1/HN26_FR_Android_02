package fa.training.entities;

import java.io.Serial;
import java.util.*;
public class FresherCandidate extends Candidate {
    @Serial
    private static final long serialVersionUID = 1L;
    private Date graduationDate;
    private String graduationRank;
    private String graduation;

    public FresherCandidate(){
        super();
    }

    public Date getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(Date graduationDate) {
        this.graduationDate = graduationDate;
    }

    public String getGraduationRank() {
        return graduationRank;
    }

    public void setGraduationRank(String graduationRank) {
        this.graduationRank = graduationRank;
    }

    public String getGraduation() {
        return graduation;
    }

    public void setGraduation(String graduation) {
        this.graduation = graduation;
    }

    public FresherCandidate(String firstName, String lastName, Date birthDate, String address, String phone, String email, Date graduationDate, String graduationRank, String graduation){
        super(firstName, lastName, birthDate, address, phone, email);
        this.graduationDate = graduationDate;
        this.graduationRank = graduationRank;
        this.graduation = graduation;


    }
}
