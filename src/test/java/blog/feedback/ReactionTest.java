package blog.feedback;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReactionTest {

    @Test
    public void add_a_reaction() {
        String type = "like";
        Reaction reaction = new Reaction(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), type);

        assertEquals(type, reaction.getType());
    }
}
