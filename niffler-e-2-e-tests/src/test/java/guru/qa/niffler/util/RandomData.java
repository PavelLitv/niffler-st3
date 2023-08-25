package guru.qa.niffler.util;

import com.github.javafaker.Faker;

public class RandomData {

    private static Faker faker = new Faker();

    public static String getName(){
        return faker.name().firstName();
    }

    public static String getPassword(){
        return faker.internet().password();
    }
}
