import java.util.*;

public class Leo {

    private ArrayList<Task> taskList = new ArrayList<>();

    public static void main(String[] args) throws LeoException{
        // String logo = " ____ _ \n"
        // + "| _ \\ _ _| | _____ \n"
        // + "| | | | | | | |/ / _ \\\n"
        // + "| |_| | |_| | < __/\n"
        // + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from Argentina, I'm Leo!\n" + "What can I do for you?");
        new Leo().start();
    }

    public void start() throws LeoException{
        Scanner sc = new Scanner(System.in);
        String cmd = sc.nextLine(); // reads in command fed by user

        while (!cmd.equals("bye")) {
            processRequest(cmd);
            cmd = sc.nextLine();
        }
        processRequest(cmd);
        sc.close();
    }

    private void addTask(String taskName, char type) throws LeoException{
        if (taskName.split(" ").length <= 1) {
            throw new IllegalArgumentException("Task description cannot be empty but...can you do it on a rainy night in Stoke?\n");
        } else {
            taskList.add(Task.createTask(taskName, type));
            System.out.printf("added: %s\n", taskList.get(taskList.size() - 1));
            System.out.printf("You have %d tasks in your list, vamos, get moving!\n", taskList.size());
        }
    }

    private void processRequest(String cmd) throws LeoException{
        for (int i = 0; i < 25; i++) {
            System.out.print("*");
        }
        System.out.println();

        if (cmd.equals("bye")) {
            System.out.println("It was nice talking, see you soon!");
        } else if (cmd.equals("list")) {
            if (taskList.isEmpty()) {
                System.out.println("You have no tasks to do!");
            } else {
                System.out.println("Here are your tasks you legend: ");
                for (int i = 0; i < taskList.size(); i++) {
                    System.out.printf("%d) %s\n", i + 1, taskList.get(i));
                }
            }
        } else if (cmd.contains("mark")) {
            if (taskList.isEmpty()) {
                System.out.println("You have no tasks to mark!");
            } else {
                String[] cmdArr = cmd.split(" ");
                int cmdIdx = Integer.parseInt(cmdArr[1]) - 1;
                if (cmdArr[0].equals("mark")) {
                    taskList.get(cmdIdx).setDone();
                    System.out.println("Well done on completing the task! Let me mark that as done! Campeon del mundo!");
                } else {
                    taskList.get(cmdIdx).resetDone();
                    System.out.println("Ok, I've marked that as not done! Please get to it :(");
                }
                System.out.printf("  %s\n", taskList.get(cmdIdx));
            }
        } else {
            char cmdlet = cmd.toLowerCase().charAt(0);
            switch (cmdlet) {
                case 't':
                    addTask(cmd, 't');
                    break;
                case 'd':
                    addTask(cmd, 'd');
                    break;
                case 'e':
                    addTask(cmd, 'e');
                    break;
                default:
                    throw new EmptyTaskException();
            }
        }
        for (int i = 0; i < 25; i++) {
            System.out.print("*");
        }
        System.out.println();
    }
}

class Task {
    private String taskName;
    private boolean isDone = false;
    private char type;

    private Task(String taskName, char type) {
        this.taskName = taskName;
        this.type = type;
    }

    public static Task createTask(String taskName, char type) throws LeoException {
        if (type == 'd') {
            String[] taskData = taskName.split("/");
            if (taskData.length <= 1) {
                throw new MissingDeadlineException();
            }
            return new Deadline(taskName.substring(9).split("/")[0], 'D', taskName.split("/")[1]);
        } if (type == 'e') { 
            String[] taskData = taskName.split("/");
            if (taskData.length < 3) {
                throw new MissingTimelineException();
            }
            return new Event(taskName.substring(6).split("/")[0], 'E', taskName.split("/")[1], taskName.split("/")[2]);
        }
        return new Todo(taskName.substring(5), 'T');
    }

    public void setDone() {
        isDone = true;
    }

    public void resetDone() {
        isDone = false;
    }

    public char getDone() {
        return isDone ? 'X' : ' ';
    }

    public char getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("[%c][%c] %s", getType(), getDone(), taskName);
    }

    private static class Todo extends Task {
        public Todo(String taskName, char type) {
            super(taskName, type);
        }
    }

    private static class Deadline extends Task {
        String by;

        public Deadline(String taskName, char type, String by) {
            super(taskName, type);
            this.by = by.substring(3);
        }

        @Override
        public String toString() {
            return String.format("%s (by: %s)", super.toString(), this.by);
        }
    }

    private static class Event extends Task {
        String from;
        String to;

        public Event(String taskName, char type, String from, String to) {
            super(taskName, type);
            this.from = from.substring(5);
            this.to = to.substring(3);
        }

        @Override
        public String toString() {
            return String.format("%s (from: %s, to: %s)", super.toString(), from, to);
        }
    }

}

class LeoException extends Exception {
    LeoException(String exceptionStr) {
        super(exceptionStr);
    }
}

class EmptyTaskException extends LeoException {
    EmptyTaskException() {
        super("I'm sorry, I don't know what you want. ¿Que miras bobo?\n");
    }
}

class MissingDeadlineException extends LeoException {
    MissingDeadlineException() {
        super("When is it due bruv? I never make predictions, and I never will.\n");
    }
}

class MissingTimelineException extends LeoException {
    MissingTimelineException() {
        super("Not this again, indicate a from and to. I'm as happy as I can be—but I have been happier.\n");
    }
}