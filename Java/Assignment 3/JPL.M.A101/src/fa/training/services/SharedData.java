package fa.training.services;

import fa.training.entities.Candidate;
import fa.training.entities.ExperienceCandidate;
import fa.training.entities.FresherCandidate;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SharedData {
    private final String FILE_NAME = "candidates.txt";
    private int status = 1;
    private List<Candidate> cachedList = new ArrayList<>();

    // Luồng 1: Ghi dữ liệu vào file text
    public synchronized void writeToFile(List<Candidate> candidates) {
        while (status != 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\n[Thread 1] Da khoa file. Dang ghi danh sach vao file text...");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(candidates);
            System.out.println("[Thread 1] Ghi file thanh cong. Giai phong file.");
        } catch (IOException e) {
            System.out.println("[Thread 1] Loi ghi file: " + e.getMessage());
        }

        status = 2;
        notifyAll();
    }

    // Luồng 2: Đọc dữ liệu từ file text
    @SuppressWarnings("unchecked")
    public synchronized void readFromFile() {
        while (status != 2) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("[Thread 2] Da khoa file. Dang doc danh sach tu file text...");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            cachedList = (List<Candidate>) ois.readObject();
            System.out.println("[Thread 2] Doc file thanh cong. Giai phong file.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[Thread 2] Loi doc file: " + e.getMessage());
        }

        status = 3;
        notifyAll();
    }

    // Luồng 3: Hiển thị dữ liệu dạng bảng
    public synchronized void displayCandidates() {
        while (status != 3) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\n[Thread 3] Hien thi danh sach ung vien theo format bang:");

        // Tạo đường viền bảng dài để chứa đủ thông tin
        String border = new String(new char[165]).replace('\0', '-');
        System.out.println(border);

        // In tiêu đề các cột
        System.out.printf("| %-12s | %-12s | %-12s | %-12s | %-12s | %-20s | %-12s | %-45s |\n",
                "First Name", "Last Name", "Birth Date", "Address", "Phone", "Email", "Type", "Specific Details");
        System.out.println(border);

        SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (Candidate c : cachedList) {
            String type = "";
            String details = "";
            String formattedBirthDate = displayFormat.format(c.getBirthDate());

            // Ép kiểu để lấy thông tin riêng của từng loại Candidate
            if (c instanceof ExperienceCandidate) {
                type = "Experience";
                ExperienceCandidate exp = (ExperienceCandidate) c;
                details = String.format("Exp: %d yrs, Skills: %s",
                        exp.getYearExperience(), exp.getProfessionalSkill());

            } else if (c instanceof FresherCandidate) {
                type = "Fresher";
                FresherCandidate fresher = (FresherCandidate) c;
                String formattedGradDate = displayFormat.format(fresher.getGraduationDate());
                details = String.format("Grad: %s, Rank: %s, Edu: %s",
                        formattedGradDate, fresher.getGraduationRank(), fresher.getGraduation());
            }

            // In ra toàn bộ thông tin trên một hàng
            System.out.printf("| %-12s | %-12s | %-12s | %-12s | %-12s | %-20s | %-12s | %-45s |\n",
                    c.getFirstName(), c.getLastName(), formattedBirthDate,
                    c.getAddress(), c.getPhone(), c.getEmail(), type, details);
        }
        System.out.println(border);

        status = 1;
        notifyAll();
    }
}