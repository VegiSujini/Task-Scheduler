import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class TaskScheduler {
    private Queue<Task> taskQueue = new ConcurrentLinkedDeque<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    public void addTask(Task task) {
        taskQueue.add(task);
    }

    public void executeTasks(Consumer<Task> taskConsumer) {
        while (!taskQueue.isEmpty()) {
            Task task = taskQueue.poll();
            executorService.submit(() -> {
                try {
                    taskConsumer.accept(task);
                    task.setCompleted(true);
                    System.out.println("Task " + task.getName() + " completed successfully.");
                } catch (Exception exception) {
                    System.out.println("Error executing task " + task.getName() + ":" + exception.getMessage());
                } finally {
                    if (!task.isCompleted()) {
                        System.out.println("Retrying task " + task.getName() + "...");
                        taskQueue.add(task);
                    }
                }
            });
        }
    }

    public List<Task> getPendingTasks() {
        return new ArrayList<>(taskQueue);
    }

    public void shutdownExecutor() {
        executorService.shutdown();
    }
}
