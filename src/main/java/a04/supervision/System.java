package a04.supervision;

import akka.actor.typed.ActorSystem;

public class System {
    public static void main(String[] args) throws InterruptedException {
        ActorSystem<String> testSystem = ActorSystem.create(ParentActor.create(), "testSystem");

        testSystem.tell("OK");
        testSystem.tell("OK");

        testSystem.tell("resume");
        testSystem.tell("OK");
        testSystem.tell("OK");

        testSystem.tell("restart");
        testSystem.tell("OK");

        Thread.sleep(3000);
        testSystem.terminate();
    }
}
