package a03.ask;

import akka.actor.typed.ActorSystem;

public class System {
    public static void main(String[] args) throws InterruptedException {
        ActorSystem<ParentActor.Message> testSystem = ActorSystem.create(ParentActor.create(), "testSystem");

        testSystem.tell(new ParentActor.SendRequest());

        Thread.sleep(3000);
        testSystem.terminate();
    }
}
