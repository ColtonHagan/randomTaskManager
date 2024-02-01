import java.util.*;
import java.text.ParseException;
import java.lang.NumberFormatException;
import java.text.SimpleDateFormat;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

class TaskManager {
    private static List<Task> tasks = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final String DIR = "C:\\Users\\Corton\\Documents\\Random Coding"; // because i don't want to bother fixing virtual code directory.

    public static void main(String[] args) {
        loadTasks();
        taskManagement();
        saveTasks();
    }

    private static void taskManagement() {
        boolean exit = false;
        while (!exit) {
            printMenu();
            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    viewTasks();
                    break;
                case 3:
                    completeTask();
                    break;
                case 4:
                    deleteTask();
                    break;
                case 5:
                    editTask();
                    break;
                case 6:
                    sortTasks();
                    break;
                case 7:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void loadTasks() {
        String saves = saveFilesList(false);
        if (saves == null || saves.length() == 0)
            return;
        if (yOrN("Would you like to load existing tasks?")) {
            System.out.println(saves);
            String[] savesArr = saves.split("\\r?\\n");
            int saveNum = getSaveFileNumber(savesArr.length);
            if (saveNum == -1)
                return;
            parseTaskFile(DIR + "\\taskFiles\\" + (savesArr[saveNum - 1].replaceAll(".*?([a-zA-Z].*)", "$1")) + ".txt");
        }
    }

    private static int getSaveFileNumber(int saves) {
        System.out.print("Select save # (-1 to exit): ");
        int userInput = getInt();
        if (userInput == -1)
            return -1;
        else if (userInput >= 1 && userInput <= saves)
            return userInput;
        else {
            System.out.println("Please enter save # coresponding to save. Please try again");
            return getSaveFileNumber(saves);
        }

    }

    private static void parseTaskFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] lineInfo = line.split(",");
                if (lineInfo.length != 4) {
                    System.out.println("Error: save file is not valid");
                    return;
                }
                tasks.add(new Task(lineInfo[0], lineInfo[1], lineInfo[2], Boolean.parseBoolean(lineInfo[3])));
            }
        } catch (IOException e) {
            System.out.println("Error: loading save file path");
            e.printStackTrace();
        }
    }

    private static void saveTasks() {
        if (yOrN("Would you like to save your tasks?")) {
            writeSaveData(createFile());
        }
    }

    private static String createFile() {
        System.out.print("Enter the save name: ");
        String filePath = scanner.nextLine();

        filePath = DIR + "\\taskFiles\\" + (filePath.endsWith(".txt") ? filePath : filePath + ".txt");

        if (fileExists(filePath)) {
            System.out.println("Save file with this name already exists.");
            System.out.print("Existing saves: " + saveFilesList(true));
            return createFile();
        }
        return filePath;
    }

    private static String saveFilesList(boolean list) {
        StringBuilder fileList = new StringBuilder();
        File directory = new File(DIR + "\\taskFiles");
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    String fileName = files[i].getName().replace(".txt", "");
                    if (list) {
                        fileList.append(fileName);
                        if (i < files.length - 1) {
                            fileList.append(", ");
                        }
                    } else {
                        fileList.append((i + 1) + ": " + fileName + "\n");
                    }
                }
            } else {
                return null;
            }
        } else {
            System.out.println("Error: Directory path");
        }
        return fileList.toString();
    }

    private static void writeSaveData(String filePath) {
        BufferedWriter bufferedWriter = null;
        try {
            FileWriter fileWriter = new FileWriter(filePath, false);
            bufferedWriter = new BufferedWriter(fileWriter);

            for (Task task : tasks) {
                bufferedWriter.write(task.storeString());
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing to save file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean fileExists(String filePath) {
        return new java.io.File(filePath).exists();
    }

    private static boolean yOrN(String msg) {
        System.out.print(msg + " (y or n) ");
        String userInput = scanner.nextLine().toLowerCase().trim();
        if (userInput.equals("y"))
            return true;
        else if (userInput.equals("n"))
            return false;
        else {
            System.out.println("Please enter y or n. Please try again");
            return yOrN(msg);
        }
    }

    private static int getInt() {
        int option = 0;
        try {
            option = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Not a number. Please try again");
            System.out.print("Enter number: ");
            return getInt();
        }
        return option;
    }

    private static void printMenu() {
        System.out.println("\nTask Manager Menu:");
        System.out.println("1. Add Task");
        System.out.println("2. View Tasks");
        System.out.println("3. Mark Task Completed");
        System.out.println("4. Delete Task");
        System.out.println("5. Edit Task");
        System.out.println("6. Sort Tasks");
        System.out.println("7. Exit");
        System.out.println();
    }

    private static int getUserChoice() {
        System.out.print("Enter your choice: ");
        return getInt();
    }

    private static void addTask() {
        String name = enterTitle();
        String desc = enterDescription();
        Date date = getDate();
        tasks.add(new Task(name, desc, date));
    }

    private static String enterTitle() {
        System.out.print("Enter task title: ");
        return scanner.nextLine();
    }

    private static String enterDescription() {
        System.out.print("Enter task description: ");
        return scanner.nextLine();
    }

    private static boolean enterCompletion() {
        System.out.print("Enter completion status (true or false): ");
        String userInput = scanner.nextLine();
        try {
            return Boolean.parseBoolean(userInput);
        } catch (NumberFormatException e) {
            System.out.println("Please enter true or false. Please try again");
            return enterCompletion();
        }
    }

    private static Date getDate() {
        System.out.print("Enter a date (YYYY-MM-DD): ");
        String userInput = scanner.nextLine();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(userInput);
        } catch (ParseException e) {
            System.out.println("Please enter the date in YYYY-MM-DD format. Please try again");
            return getDate();
        }
    }

    private static void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ": " + tasks.get(i).toString());
            }
        }
    }

    private static void completeTask() {
        int task = selectTask("mark completed");
        if (task == -1)
            return;
        tasks.get(task).setCompleted(true);
    }

    private static void deleteTask() {
        int task = selectTask("edit");
        if (task == -1)
            return;
        tasks.remove(task);
    }

    private static void editTask() {
        int taskNum = selectTask("edit");
        if (taskNum == -1)
            return;
        Task task = tasks.get(taskNum);
        printTaskInfo(task);
        System.out.print("What aspect would you like to edit: ");
        int editNum = getInt();
        if (editNum == 1) { // title
            task.setTitle(enterTitle());
        } else if (editNum == 2) { // description
            task.setDescription(enterDescription());
        } else if (editNum == 3) { // due date
            task.setDate(getDate());
        } else if (editNum == 4) { // completion
            task.setCompleted(enterCompletion());
        } else {
            System.out.println("invalid number selected. Please try again");
            editTask();
        }
    }

    private static void sortTasks() {
        printTaskInfo(null);
        System.out.print("What aspect would you like to sort by: ");
        int sorter = getInt();
        if (sorter == 1) { // title
            tasks.sort(Comparator.comparing(Task::getTitle));
        } else if (sorter == 2) { // description
            tasks.sort(Comparator.comparing(Task::getDescription));
        } else if (sorter == 3) { // due date
            tasks.sort(Comparator.comparing(Task::getDate));
        } else if (sorter == 4) { // completion
            tasks.sort(Comparator.comparing(Task::getCompleted));
        } else {
            System.out.println("invalid number selected. Please try again");
            sortTasks();
        }
    }

    public static void sortTaskHelper(List<Task> tasks, Comparator<Task> comparator) {
        tasks.sort(comparator);
    }

    private static void printTaskInfo(Task task) {
        if (task != null) {
            System.out.println("1. Title: " + task.getTitle());
            System.out.println("2. Description: " + task.getDescription());
            System.out.println("3. Due Date: " + task.getDateString());
            System.out.println("4. Completion Status: " + task.getCompletedString());
        } else {
            System.out.println("1. Title");
            System.out.println("2. Description");
            System.out.println("3. Due Date");
            System.out.println("4. Completion Status");
        }
        System.out.println();
    }

    private static int selectTask(String msg) {
        viewTasks();
        System.out.print("What task # to " + msg + " (-1 to exit): ");
        int selected = getInt();
        if (isValidTaskNumber(selected)) {
            return selected - 1;
        } else if (selected == -1) {
            return -1;
        } else {
            System.out.println("Invalid task number. Please try again.");
            return selectTask(msg);
        }
    }

    private static boolean isValidTaskNumber(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }
}

class Task {
    private String title = null, description = null;
    private Date dueDate = null;
    private boolean completed = false;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Task(String title, String description, Date dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public Task(String title, String description, String dueDate, boolean completed) {
        this.title = title;
        this.description = description;
        setDateString(dueDate);
        this.completed = completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setDate(Date date) {
        dueDate = date;
    }

    public void setDateString(String date) {
        try {
            dueDate = dateFormat.parse(date);
        } catch (ParseException e) {
            System.out.println("Error: Invalid date format");
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getCompleted() {
        return completed;
    }

    public String getCompletedString() {
        return completed ? "COMPLETED" : "UNFINISHED";
    }

    public Date getDate() {
        return dueDate;
    }

    public String getDateString() {
        return dateFormat.format(dueDate);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return title + " - " + description + " DUE: " + getDateString() + " " + getCompletedString();
    }

    public String storeString() {
        return title + "," + description + "," + getDateString() + "," + completed;
    }

}