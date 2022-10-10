package a02.simpletell;

import akka.actor.typed.ActorSystem;

public class System {
    public static void main(String[] args) throws InterruptedException {
        ActorSystem<String> testSystem = ActorSystem.create(ParentActor.create(), "testSystem");

        testSystem.tell("start");

        Thread.sleep(3000);
        testSystem.terminate();
    }
}
