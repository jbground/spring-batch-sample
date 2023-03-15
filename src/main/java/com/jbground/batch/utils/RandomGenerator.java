package com.jbground.batch.utils;

import com.jbground.batch.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RandomGenerator {
    private static final String[] names = {"Aaron", "Abraham", "Adam", "Allan", "Alber", "Alexander"
            , "Alfred", "Andrew", "Antony", "Arnold", "Arthur", "Benedict", "Benjamin"
            , "Cecil", "Charles", "Claude", "Conrad", "Daniel", "David", "Donald"
            , "Douglas", "Duncan", "Edgar", "Edmund ", "Edward ", "Edwin", "Enoch", "Evelyn"
            , "Frederick", "Gabriel", "Geoffrey", "George", "Gilbert", "Gregory", "Harold", "Henry", "Hugh", "Issac"
            , "Jacob", "Jerome", "John", "Kenneth", "Lawrence", "Leonard", "Leslie"
            , "Louis", "Martin", "Matthew ", "Michael", "Nicholas", "Noel", "Oliver"
            , "Oscar", "Owen", "Patricia", "Patrick", "Paul", "Peter", "Philip", "Richard"
            , "Robert", "Roland", "Samuel ", "Theodore", "Thomas", "Vincent", "Wallace", "Walter", "William", "Agatha"
            , "Abigail", "Agnes", "Aileen", "Alexandria", "Alexis", "Alice", "Alicia", "Alicia", "Allison"
            , "Alyssa", "Amanda", "Amy", "Andrea"
            , "Angela", "Anna", "Arial", "Ashley", "Bailey", "Barbara", "Beatrice", "Breanna", "Briana"
            , "Brianna", "Brittany", "Brooke", "Caroline", "Cassidy", "Catherin", "Catherine"
            , "catherin", "Charlotte", "Cherry", "Chloe", "Christina", "Cordelia", "Courtney"
            , "Danielle", "Denise", "Destiny", "Dorothy", "Edith", "Elizabeth", "Emery"
            , "Emily", "Emily", "Emma", "Erica", "Erin", "Esther", "Faith", "Florence", "Frances"
            , "Gabrielle", "Gaby", "Gertrude", "Grace", "Haley", "Hannah", "Heather", "Helen", "Irene"
            , "Jacqueline", "Jade ", "Jessica", "Jinny", "Judith", "Julia ", "Katelyn", "Katherine", "Kathryn "
            , "Katie ", "Kayla", "Kaylee", "Kelly", "Kiara ", "Kimberly", "Kylie", "Laura", "Lauren", "Ling"
            , "Lisa", "Lucy", "Mackenzie ", "Madeline ", "Madison", "Margaret", "Mariah ", "Marissa ", "Martha"
            , "Mary", "Matilda", "Megan", "Melanie", "Melissa", "Michelle", "Mikayla", "Morgan", "Nancy"
            , "Naomi", "Natalie", "Nicole", "Niki", "Olivia", "Paige", "paula", "Phyllis", "Rachel", "Rebecca"
            , "Rebecca ", "Riley", "Rosemary ", "Sabina ", "Sabrina", "Samantha ", "Sara ", "Sarah ", "Savannah"
            , "shara", "Sharon", "Shelby", "Sierra", "Silvester", "Sophia", "Sophia", "Sophia", "Stephanie"
            , "sue", "Sydney", "Taylor", "Tracy", "Vanessa", "Victoria", "Winifred"};


    private static final Random random = new Random();

    public static String nextName() {
        int i = random.nextInt(names.length);
        return names[i];
    }

    public static String nextId(){
        return UUID.randomUUID().toString();
    }

    public static User nextUser() {
        User user = new User();
        user.setName(nextId());
        user.setName(nextName());
        return user;
    }

    public static List<User> nextUserList(int start, int row) {
        List<User> result = new ArrayList<>();

        for(int i = start ; i < (row*start)+100 ; i++){
            User user = new User();
            user.setId(nextId());
            user.setName(nextName());
            user.setNum(i);

            result.add(user);
        }

        return result;
    }
}