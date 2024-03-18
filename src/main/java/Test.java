
public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String query = "SELECT * FROM \"" + 1 + "_" + "54.89.211.121" + "\" WHERE time < " + "1708314718000000000"
//				+ " GROUP BY time(5s);";
//
//		String q = "SELECT mean(totalMemory),mean(usage),mean(usedMemory) FROM \"" + 525 + "_" + "54.89.211.121" + "\" WHERE time > " + "1708078338036000000"
//				+ " AND time < " + "1708078453013000000" + " GROUP BY time(5s);";
//
//		System.out.println(q);
		
		String error = "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJQU0F1dGhTZXJ2ZXIiLCJ1c2VySWQiOjM4LCJpcEFkZHJlc3MiOiIwOjA6MDowOjA6MDowOjEiLCJpYXQiOjE3MDg1OTk5MjEsImV4cCI6MTcxMDMwNDg5MywianRpIjoiNjI3NzgwMjMtZjQ0NS00NTc3LWJlYjUtYTk0NmE5YWY3NzBiIiwibmJmIjoxNzA4NTk5OTIyfQ.LTRfzst8NJUu9VLFhJQeGOmDKmccaDxmhlfHXLE9bbNlWb0owEgmW09Z1k2uqfG0PlhXNtjeIFT0swEmJuH2KA";

		String correct = "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJQU0F1dGhTZXJ2ZXIiLCJ1c2VySWQiOjM4LCJpcEFkZHJlc3MiOiIwOjA6MDowOjA6MDowOjEiLCJpYXQiOjE3MDg1OTk5MjEsImV4cCI6MTcxMDMwNDg5MywianRpIjoiNjI3NzgwMjMtZjQ0NS00NTc3LWJlYjUtYTk0NmE5YWY3NzBiIiwibmJmIjoxNzA4NTk5OTIyfQ.LTRfzst8NJUu9VLFhJQeGOmDKmccaDxmhlfHXLE9bbNlWb0owEgmW09Z1k2uqfG0PlhXNtjeIFT0swEmJuH2KA";
		
		if(error.equals(correct)) {
			System.out.println("true");
		}
	}

}
