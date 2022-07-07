package Model_DB;

public class Judge_error {
    public int result;

    public String error_message;

    public int Judge_error(int result, String error_message) {
        if (result == 1){
            System.out.println("Error, "+ error_message + " Fail!");
        }
        else if (result == 2){
            System.out.println("Some errors have occurred!");
        }
        else if (result == 3){
            System.out.println("Not Found!");
        }
        else if (result == 4){
            System.out.println("Duplicate results!");
        }
        else if (result == 5){
            System.out.println("Results do not match!");
        }
        else {
            System.exit(0);
        }
        return result;
    }
}
