package monsterstack.io.partner.common;

public interface Mapper<O,I> {
    O map(I in);
}
