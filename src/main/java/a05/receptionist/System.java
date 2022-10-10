package a05.receptionist;

import akka.actor.typed.ActorSystem;

public class System {
    public static void main(String[] args) throws InterruptedException {
        ActorSystem<Void> testSystem = ActorSystem.create(Guardian.create(), "testSystem");

        Thread.sleep(30_000);
        testSystem.terminate();
    }
}
