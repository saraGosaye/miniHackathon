import jakarta.persistence.TypedQuery;
import model.Department;
import model.Teacher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class manyToOneInteractive {
    public static void main(String[] args) {
        manyToOneInteractive();
    }

    public static void manyToOneInteractive() {
        System.out.println("Welcome to ManyToOneInteractive!");
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();
//        Transaction transaction = session.beginTransaction();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n0. Exit");
                System.out.println("1. Manage Departments");
                System.out.println("2. Manage Teachers");
                System.out.println("3. Assign Teacher to Department");
                System.out.println("4. List Teachers");
                System.out.println("5. List Department");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    case 1:
                        manageDepartments(scanner, factory);
                        break;
                    case 2:
                        manageTeachers(scanner, factory);
                        break;
                    case 3:
                        assignTeacherToDepartment(scanner, session);
                        break;
                    case 4:
                        listTeachers(session);
                        break;
                    case 5:
                        listDepts(session);
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            session.close();
            factory.close();

        }
    }

    private static void manageDepartments(Scanner scanner, @org.jetbrains.annotations.NotNull SessionFactory factory) {
        Session session = factory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            System.out.println("\n1. Add Departments");
            System.out.println("2. Delete Department");
            System.out.println("3. Modify Department");
            System.out.println("4. Go back to menu");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter Department Name: ");
                    String deptName = scanner.next();
                    Department dept = new Department(deptName);
                    session.save(dept);
                    System.out.println("Department added.");
                    break;
                case 2:
                    System.out.print("Enter Department ID to delete: ");
                    int deptId = scanner.nextInt();
                    Department deptToDelete = session.get(Department.class, deptId);
                    if (deptToDelete != null) {
                        session.delete(deptToDelete);
                        System.out.println("Department deleted.");
                    } else {
                        System.out.println("Department not found.");
                    }
                    break;
                case 3:
                    System.out.print("Enter Department ID to modify: ");
                    int deptIdToModify = scanner.nextInt();
                    Department deptToModify = session.get(Department.class, deptIdToModify);
                    if (deptToModify != null) {
                        System.out.print("Enter new Department Name: ");
                        String newDeptName = scanner.next();
                        deptToModify.setDeptName(newDeptName);
                        session.update(deptToModify);
                        System.out.println("Department modified.");
                    } else {
                        System.out.println("Department not found.");
                    }
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static void manageTeachers(Scanner scanner, SessionFactory factory) {
        Session session = factory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            System.out.println("\n1. Add Teachers");
            System.out.println("2. Delete Teacher");
            System.out.println("3. Modify Teacher");
            System.out.println("4. Go back to menu");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter Teacher Name: ");
                    String teacherName = scanner.next();
                    Teacher teacher = new Teacher(teacherName, null);
                    session.save(teacher);
                    System.out.println("Teacher added.");
                    break;
                case 2:
                    System.out.print("Enter Teacher ID to delete: ");
                    int teacherId = scanner.nextInt();
                    Teacher teacherToDelete = session.get(Teacher.class, teacherId);
                    if (teacherToDelete != null) {
                        session.delete(teacherToDelete);
                        System.out.println("Teacher deleted.");
                    } else {
                        System.out.println("Teacher not found.");
                    }
                    break;
                case 3:
                    System.out.print("Enter Teacher ID to modify: ");
                    int teacherIdToModify = scanner.nextInt();
                    Teacher teacherToModify = session.get(Teacher.class, teacherIdToModify);
                    if (teacherToModify!= null) {
                        System.out.print("Enter new Teacher Name: ");
                        String newTeacherName = scanner.next();
                        teacherToModify.setTeacherName(newTeacherName);
                        session.update(teacherToModify);
                        System.out.println("Teacher modified.");
                    } else {
                        System.out.println("Teacher not found.");
                    }
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static void assignTeacherToDepartment(Scanner scanner, Session session) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            System.out.print("Which Teacher would you like to modify? Enter Teacher ID:" );
            int teacherId = scanner.nextInt();
            Teacher teacher = session.get(Teacher.class, teacherId);
            if (teacher != null) {
                System.out.print("Which department would you like to assign to Teacher? Enter Department ID: ");
                int deptId = scanner.nextInt();
                Department department = session.get(Department.class, deptId);
                if (department != null) {
                    teacher.setDepartment(department);
                    session.update(teacher);
                    System.out.println("Teacher assigned to department.");
                } else {
                    System.out.println("Department not found.");
                }
            } else {
                System.out.println("Teacher not found.");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }


    private static void listDepts(Session session) {
        TypedQuery<Department> query = session.createQuery("FROM Department", Department.class);
        List<Department> departments = query.getResultList();
        for (Department department : departments) {
            System.out.println(department.getDeptId() + ": " + department.getDeptName());
        }
    }


    private static void listTeachers(Session session) {
        TypedQuery<Teacher> query = session.createQuery("FROM Teacher", Teacher.class);
        List<Teacher> teachers = query.getResultList();
        for (Teacher teacher : teachers) {
            System.out.println(teacher.getTeacherId() + ": " + teacher.getTeacherName());
        }
    }
}
