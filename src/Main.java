public class Main {
    public static void main(String[] args) {
        ExternalChainingHashMap<Integer, String> phoneNum = new ExternalChainingHashMap<>();
        phoneNum.put(3736, "Scott");
        phoneNum.put(2244, "Kayla");
        phoneNum.put(3678, "Gatsby");
        phoneNum.put(5, "Judy");
        System.out.println(phoneNum.put(3736, "Scotty"));
        phoneNum.put(5655, "Dad");
        phoneNum.put(3736, "Jan");
        phoneNum.put(1333, "Jude");
        phoneNum.put(2323, "Josh");
        phoneNum.put(1222, "Murphy");
        phoneNum.put(253, "Doctor");
        phoneNum.put(2111, "Jesse");
        phoneNum.put(37, "David");
        System.out.println("hey");
        System.out.println(phoneNum.remove(37));
        System.out.println(phoneNum.remove(253));
    }
}

