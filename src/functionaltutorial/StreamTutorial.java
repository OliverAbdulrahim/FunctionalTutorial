package functionaltutorial;

import functionaltutorial.Person.Gender;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * This class contains examples of the functional programming features added in
 * Java 8.
 * 
 * @author Oliver Abdulrahim
 */
public class StreamTutorial {

    /**
     * Stores a roster of people.
     * 
     * @see functionaltutorial.Person
     */
    public static final List<Person> PEOPLE = new ArrayList<>();
    
    /**
     * Defines the amount of people to store in {@link #PEOPLE}.
     */
    public static final int POPULATION = 100;
    
    /**
     * Initializes the people for this tutorial.
     */
    static {
        for (int i = 0; i < POPULATION; i++) {
            String name = randomString('a', 'z', randomInt(5, 10));
            name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
            int age = randomInt(5, 75);
            Gender g = randomInt(1, 2) == 1 ? Gender.MALE : Gender.FEMALE; 
            PEOPLE.add(new Person(name, g, age));
        }
    }
    
    /**
     * Generates a pseudorandom {@code String} object. May include {@code char}
     * values from {@code lower} to {@code upper} inclusive.
     * 
     * @param lower Lower bound, inclusive.
     * @param upper Upper bound, inclusive.
     * @param length The length of the {@code String} to generate.
     * @return A random {@code String} object with the given length.
     * @see #randomInt(int, int) Casts the result of this method to a 
     *      <code>char</code>.
     */
    public static String randomString(char lower, char upper, int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) randomInt(lower, upper));
        }
        return sb.toString();
    }
    
    /**
     * Generates a pseudorandom {@code int} value, from the given lower bound
     * to the given upper bound, inclusive.
     * 
     * @param lower The lower bound to generate, inclusive.
     * @param upper The Upper bound to generate, inclusive.
     * @return A random {@code int}.
     */
    public static int randomInt(int lower, int upper) {
        return ThreadLocalRandom.current().nextInt(lower, upper + 1);
    }
    
    /**
     * Returns a list containing all people whose ages are within the given 
     * range of using an imperative implementation. 
     * 
     * @param lowerAge The lower bound for the ages to include, inclusive.
     * @param upperAge The upper bound for the ages to include, exclusive.
     * @return A list containing all people within the given ages.
     */
    public static List<Person> getAllWithin0(int lowerAge, int upperAge) {
        List<Person> ageGroup = new ArrayList<>();
        for (Person p : PEOPLE) {
            if (p.getAge() >= lowerAge && p.getAge() < upperAge) {
                ageGroup.add(p);
            }
        }
        return ageGroup;
    }
    
    /**
     * Returns a list containing all people whose ages are within the given 
     * range of using a functional implementation. 
     * 
     * <p> Often, it is necessary to process only a certain part of a list. This
     * method uses an un-idiomatic functional approach to collecting all
     * elements in {@link #PEOPLE} that match the given age arguments. Below is
     * the logic contained within this method:
     * 
     * <pre>{@code
     *     PEOPLE
     *       .stream()
     *       .filter(p -> p.getAge() >= lowerAge && p.getAge() < upperAge)
     *       .collect(Collectors.toList());
     * }</pre>
     * 
     * <p> The first part of the code:
     * 
     * <pre>{@code
     *     PEOPLE
     *       .stream()
     *     ...
     * }</pre>
     * 
     * selects the {@code stream()} method from the {@code PEOPLE} ArrayList.
     * This method returns a {@code Stream} of {@code Person} objects, or more 
     * formally, an object of {@code Stream<Person>}. A {@code Stream} is simply
     * a "sequence of elements" upon which functions may be applied 
     * (<a href="https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html">see
     * for detailed information</a>). All {@code Iterable} objects, and by 
     * extension all {@code Collection} objects, may choose to stream their 
     * contents.
     *
     * <p> The next part of the code:
     * 
     * <pre>{@code
     *      ...
     *       .filter(p -> p.getAge() >= lowerAge && p.getAge() < upperAge)
     *      ...
     * }</pre>
     * 
     * selects the {@code filter(Predicate<Person>)} method from the stream
     * retrieved from the previous line's call. A {@code Predicate} is a special
     * type of boolean-valued function that takes a single argument. In this 
     * case, the desired behavior is to remove, or filter out, any 
     * {@code Person} objects that are not within the given range of ages. In
     * order to create an object of {@code Predicate}, it is required to either:
     * 
     * <ul>
     *   <li> Create a class that implements the interface,
     *   <li> Create an anonymous inner class, or
     *   <li> Use a lambda expression.
     *     <ul>
     *       <li> See the lambda expression tutorial (TODO!) in order to 
     *            understand the basics of Java 8 functional programming.
     *     </ul>
     * </ul>
     * 
     * This implementation utilizes a lambda expression, which is indicated by
     * the <em>lambda operator</em> ({@code ->}). If the given is written more 
     * explicitly, its purpose becomes more clear:
     * 
     * <pre>{@code
     *      ...
     *       .filter((Person p) -> {
     *           return p.getAge() >= lowerAge && p.getAge() < upperAge;
     *       })
     *      ...
     * }</pre>
     * 
     * Note the un-inferred parameter type. Each person's age in the 
     * {@code Stream} is tested against the given arguments. If the person's age
     * is within the given range, then it "falls through" the imaginary filter 
     * and is added to an aggregate {@code Stream}. This makes {@code filter} an
     * <em>intermediate operation</em>, or more specifically, a 
     * <em>non-stateful</em> one. This means that the method processes and 
     * returns a new {@code Stream} with the objects that pass through the 
     * filter.
     * 
     * <p> The final part of the code:
     * 
     * <pre>{@code
     *      ...
     *       .collect(Collectors.toList()); 
     *      ...
     * }</pre>
     * 
     * transforms the elements of the {@code Stream} returned by the previous 
     * filter operation into a {@code List<Person>} object using the 
     * {@code collect(Collector<? super Person, List, List<Person>)} method. 
     * This makes {@code collect} a <em>terminal operation</em> which means that
     * the method returns a non-{@code Stream} result. This value is then 
     * returned by the method.
     * 
     * @param lowerAge The lower bound for the ages to include, inclusive.
     * @param upperAge The upper bound for the ages to include, exclusive.
     * @return A list containing all people within the given ages.
     */
    public static List<Person> getAllWithin1(int lowerAge, int upperAge) {
        return PEOPLE
                .stream()
                .filter(p -> p.getAge() >= lowerAge && p.getAge() < upperAge)
                .collect(Collectors.toList());
    }
    
    /**
     * Returns the average age of all the {@code Person} objects contained in 
     * {@link #PEOPLE} using an imperative implementation. 
     * 
     * @return The average age of all people.
     */
    public static double getAverageAge0() {
        double average = 0.0d;
        for (Person p : PEOPLE) {
            average += p.getAge();
        }
        return average / PEOPLE.size();
    }
    
    /**
     * Returns the average age of all the {@code Person} objects contained in 
     * {@link #PEOPLE} using a functional implementation.
     * 
     * <p> TODO - Notes:
     *   average() is a terminal operation. Also a special type of reduction
     *   mapToDouble is a intermediate operation that changes the type of the
     *   Stream. This example uses a method reference in place of the lambda
     *   expression. Show how they are the same
     * 
     * @return The average age of all people.
     */
    public static double getAverageAge1() {
        return PEOPLE
                .stream()
                .mapToDouble(Person :: getAge)
                .average()
                .getAsDouble();
    }
    
    /**
     * TODO - Add more robust way of showing each of the methods, possible a 
     * console-driven menu.
     * 
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        System.out.println(PEOPLE);
        System.out.println(getAllWithin0(10, 20).equals(getAllWithin1(10, 20)));
    }
    
}
