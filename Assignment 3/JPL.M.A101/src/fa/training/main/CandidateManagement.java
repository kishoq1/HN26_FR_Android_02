package fa.training.main;

import fa.training.entities.Candidate;
import fa.training.entities.ExperienceCandidate;
import fa.training.entities.FresherCandidate;
import fa.training.services.SharedData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CandidateManagement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Candidate> inputList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);

        System.out.println("=== CHUONG TRINH QUAN LY UNG VIEN ===");

        while (true) {
            System.out.println("\n-------------------------------------------");
            System.out.println("Chon loai ung vien ban muon nhap:");
            System.out.println("1. Experience Candidate");
            System.out.println("2. Fresher Candidate");
            System.out.println("0. Ket thuc nhap va chay chuong trinh");
            System.out.print("Lua chon cua ban: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("0")) {
                break;
            }

            try {
                switch (choice) {
                    case "1":
                        System.out.println("\n--- NHAP THONG TIN EXPERIENCE CANDIDATE ---");
                        System.out.print("First Name: "); String fName1 = scanner.nextLine();
                        System.out.print("Last Name: "); String lName1 = scanner.nextLine();

                        System.out.print("Birth Date (yyyy-MM-dd): ");
                        Date bDate1 = sdf.parse(scanner.nextLine());

                        System.out.print("Address: "); String address1 = scanner.nextLine();
                        System.out.print("Phone: "); String phone1 = scanner.nextLine();
                        System.out.print("Email: "); String email1 = scanner.nextLine();

                        System.out.print("Years of Experience (so nguyen): ");
                        int expYears = Integer.parseInt(scanner.nextLine());

                        System.out.print("Professional Skill: "); String proSkill = scanner.nextLine();

                        // Thêm vào danh sách
                        inputList.add(new ExperienceCandidate(
                                fName1, lName1, bDate1, address1, phone1, email1, expYears, proSkill));
                        System.out.println("-> Da them thanh cong 1 Experience Candidate!");
                        break;

                    case "2":
                        System.out.println("\n--- NHAP THONG TIN FRESHER CANDIDATE ---");
                        System.out.print("First Name: "); String fName2 = scanner.nextLine();
                        System.out.print("Last Name: "); String lName2 = scanner.nextLine();

                        System.out.print("Birth Date (yyyy-MM-dd): ");
                        Date bDate2 = sdf.parse(scanner.nextLine());

                        System.out.print("Address: "); String address2 = scanner.nextLine();
                        System.out.print("Phone: "); String phone2 = scanner.nextLine();
                        System.out.print("Email: "); String email2 = scanner.nextLine();

                        System.out.print("Graduation Date (yyyy-MM-dd): ");
                        Date gradDate = sdf.parse(scanner.nextLine());

                        System.out.print("Graduation Rank (Excellent/Good/Fair): "); String rank = scanner.nextLine();
                        System.out.print("Education (University name): "); String education = scanner.nextLine();

                        // Thêm vào danh sách
                        inputList.add(new FresherCandidate(
                                fName2, lName2, bDate2, address2, phone2, email2, gradDate, rank, education));
                        System.out.println("-> Da them thanh cong 1 Fresher Candidate!");
                        break;

                    default:
                        System.out.println("Loi: Lua chon khong hop le. Vui long nhap 0, 1 hoac 2.");
                        break;
                }
            } catch (ParseException e) {
                System.out.println("Loi: Ban da nhap sai dinh dang ngay thang (Phai la yyyy-MM-dd). Vui long nhap lai tu dau cho ung vien nay.");
            } catch (NumberFormatException e) {
                System.out.println("Loi: Ban da nhap sai dinh dang so. Vui long nhap lai tu dau cho ung vien nay.");
            }
        }
        // KIỂM TRA DỮ LIỆU VÀ CHẠY ĐA LUỒNG (THREADS)
        if (inputList.isEmpty()) {
            System.out.println("\n=> Danh sach ung vien trong. Chuong trinh ket thuc.");
        } else {
            System.out.println("\n=> Ket thuc nhap lieu. Bat dau khoi chay cac Thread (Da luong)...");

            SharedData sharedData = new SharedData();

            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    sharedData.writeToFile(inputList);
                }
            });

            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    sharedData.readFromFile();
                }
            });

            Thread t3 = new Thread(new Runnable() {
                @Override
                public void run() {
                    sharedData.displayCandidates();
                }
            });

            t1.start();
            t2.start();
            t3.start();
        }

        scanner.close();
    }
}