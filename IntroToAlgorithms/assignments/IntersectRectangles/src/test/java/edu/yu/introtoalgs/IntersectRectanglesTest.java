import edu.yu.introtoalgs.IntersectRectangles;
import edu.yu.introtoalgs.IntersectRectangles.Rectangle;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntersectRectanglesTest {

  //Test from the Doc
  @Test
  public void test1() {
    System.out.println("------------");
    final Rectangle A = new Rectangle(0, 0, 2, 5);
    final Rectangle B = new Rectangle(2, 0, 2, 5);

    System.out.println(A);
    System.out.println(B);
    System.out.println(IntersectRectangles.intersect(A, B));

    assertEquals(new Rectangle(2, 0, 0, 5), IntersectRectangles.intersect(A, B));

  }

  //Opposite case of Test 1
  @Test
  public void test2() {
    System.out.println("------------");
    final Rectangle A = new Rectangle(2, 0, 2, 5);
    final Rectangle B = new Rectangle(0, 0, 2, 5);

    System.out.println(A);
    System.out.println(B);
    System.out.println(IntersectRectangles.intersect(A, B));

    assertEquals(new Rectangle(2, 0, 0, 5), IntersectRectangles.intersect(A, B));
  }

  @Test
  public void test3() {
    System.out.println("------------");
    final Rectangle A = new Rectangle(2, 2, 2, 2);
    final Rectangle B = new Rectangle(3, 0, 3, 3);

    System.out.println(A);
    System.out.println(B);
    System.out.println(IntersectRectangles.intersect(A, B));

    assertEquals(new Rectangle(3, 2, 1, 1), IntersectRectangles.intersect(A, B));
  }

  @Test
  public void test4() {
    System.out.println("------------");
    final Rectangle A = new Rectangle(0, 0, 3, 2);
    final Rectangle B = new Rectangle(2, 1, 3, 2);

    System.out.println(A);
    System.out.println(B);
    System.out.println(IntersectRectangles.intersect(A, B));

    assertEquals(new Rectangle(2, 1, 1, 1), IntersectRectangles.intersect(A, B));
  }

  @Test
  public void test5() {
    System.out.println("------------");
    final Rectangle A = new Rectangle(2, 2, 2, 2);
    final Rectangle B = new Rectangle(2, 1, 2, 2);

    System.out.println(A);
    System.out.println(B);
    System.out.println(IntersectRectangles.intersect(A, B));

    assertEquals(new Rectangle(2, 2, 2, 1), IntersectRectangles.intersect(A, B));
  }

  @Test
  public void test6() {
    System.out.println("------------");
    final Rectangle A = new Rectangle(-2, 1, 4, 3);
    final Rectangle B = new Rectangle(0, 2, 1, 3);

    System.out.println(A);
    System.out.println(B);
    System.out.println(IntersectRectangles.intersect(A, B));

    assertEquals(new Rectangle(0, 2, 1, 2), IntersectRectangles.intersect(A, B));
  }

  @Test
  public void test7() {
    System.out.println("------------");
    final Rectangle A = new Rectangle(-2, -2, 4, 2);
    final Rectangle B = new Rectangle(0, 0, 3, 2);

    System.out.println(A);
    System.out.println(B);
    System.out.println(IntersectRectangles.intersect(A, B));

    assertEquals(new Rectangle(0, 0, 2, 0), IntersectRectangles.intersect(A, B));
  }

  @Test
  public void test8() {
    System.out.println("------------");
    final Rectangle A = new Rectangle(0, 0, 1, 1);
    final Rectangle B = new Rectangle(1, 1, 1, 1);

    System.out.println(A);
    System.out.println(B);
    System.out.println(IntersectRectangles.intersect(A, B));

    assertEquals(new Rectangle(1, 1, 0, 0), IntersectRectangles.intersect(A, B));
  }

  @Test
  public void test9() {
    System.out.println("------------");
    final Rectangle A = new Rectangle(0, 0, 1, 1);
    final Rectangle B = new Rectangle(2, 2, 1, 1);

    System.out.println(A);
    System.out.println(B);
    System.out.println(IntersectRectangles.intersect(A, B));

    assertEquals(IntersectRectangles.NO_INTERSECTION, IntersectRectangles.intersect(A, B));
  }

  @Test
  public void test10() {
    System.out.println("------------");
    final Rectangle A = new Rectangle(0, 0, 4, 4);
    final Rectangle B = new Rectangle(2, 2, 1, 1);

    System.out.println(A);
    System.out.println(B);
    System.out.println(IntersectRectangles.intersect(A, B));

    assertEquals(new Rectangle(2, 2, 1, 1), IntersectRectangles.intersect(A, B));
  }








}
