import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
public class TaskSchedulerMain {
    public static void main(String[] args) {

        TaskScheduler taskScheduler = new TaskScheduler();

        taskScheduler.addTask(new Task("task 1"));
        taskScheduler.addTask(new Task("task 2"));
        taskScheduler.addTask(new Task("task 3"));

        Comsumer<Task> taskExecutor = task->{
            try {
                TimeUnit.SECONDS.sleep(1);
                if ("task 2".equals(task.getName())) {
                    throw new RuntimeException("Simulated failure for task 2.");
                }
                System.out.println("Executing " + task.getName());
            } catch (InterruptedException interruptedException) {
                System.out.println(interruptedException.getMessage());
            }
        };

        taskScheduler.executeTasks(taskExecutor);

        System.out.println("Pending tasks : ");
        taskScheduler.getPendingTasks().forEach(task->System.out.println(task.getName()));
          
        taskScheduler.shutdownExecutor();
    }
}
