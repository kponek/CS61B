import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Created by kevin on 2/4/2017.
 */
public class TestArrayDeque1B {
    @Test
    public void testDequeCall() {
        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<>();
        OperationSequence set = new OperationSequence();
        int count = 0;
        //addFirst, addLast, removeFirst, removeLast
        while (count < 500) {
            //random number from 0 to 50.0
            Integer randomNumber = StdRandom.uniform(0, 51);
            if (student.isEmpty()) {
                //only test addFirst or addLast
                //don't remove from an empty array
                if (randomNumber < 25) {
                    DequeOperation op = new DequeOperation("addFirst", randomNumber);
                    set.addOperation(op);
                    student.addFirst(randomNumber);
                    solution.addFirst(randomNumber);
//                    assertEquals(set.toString(), solution.toString(), student.toString());
                } else {
                    DequeOperation op = new DequeOperation("addLast", randomNumber);
                    set.addOperation(op);
                    student.addLast(randomNumber);
                    solution.addLast(randomNumber);
//                    assertEquals(set.toString(), solution.toString(), student.toString());
                }
            } else {
                //test any in here because the StudentArrayDeque is not empty
                if (randomNumber < 12) {
                    DequeOperation op = new DequeOperation("addFirst", randomNumber);
                    set.addOperation(op);
                    student.addFirst(randomNumber);
                    solution.addFirst(randomNumber);
//                    assertEquals(set.toString(), solution, student.toString());
                } else if (randomNumber < 25) {
                    DequeOperation op = new DequeOperation("addLast", randomNumber);
                    set.addOperation(op);
                    student.addLast(randomNumber);
                    solution.addLast(randomNumber);
//                    assertEquals(set.toString(), solution, student.toString());
                } else if (randomNumber < 38) {
                    DequeOperation op = new DequeOperation("removeFirst");
                    set.addOperation(op);
                    Integer studentRemove = student.removeFirst();
                    Integer solutionRemove = solution.removeFirst();
                    assertEquals(set.toString(), solutionRemove, studentRemove);
                } else {
                    DequeOperation op = new DequeOperation("removeLast");
                    set.addOperation(op);
                    Integer studentRemove = student.removeLast();
                    Integer solutionRemove = solution.removeLast();
                    assertEquals(set.toString(), solutionRemove, studentRemove);
                }
                count++;
            }
        }
        System.out.println(student);
        System.out.println(solution);
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestArrayDeque1B.class);
    }
}
