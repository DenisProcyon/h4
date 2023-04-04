import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Quaternions. Basic operations. */
public class Quaternion {

   double threshold = 0.001;
   private double real_part;
   private double im_i;
   private double im_j;
   private double im_k;

   /** Constructor from four double values.
    * @param a real part
    * @param b imaginary part i
    * @param c imaginary part j
    * @param d imaginary part k
    */
   public Quaternion (double a, double b, double c, double d) {
      real_part = a;
      im_i = b;
      im_j = c;
      im_k = d;
   }

   /** Real part of the quaternion.
    * @return real part
    */
   public double getRpart() {
      return real_part;
   }

   public double getIpart() {
      return im_i;
   }

   /** Imaginary part j of the quaternion. 
    * @return imaginary part j
    */
   public double getJpart() {
      return im_j;
   }

   /** Imaginary part k of the quaternion. 
    * @return imaginary part k
    */
   public double getKpart() {
      return im_k;
   }

   /** Conversion of the quaternion to the string.
    * @return a string form of this quaternion: 
    * "a+bi+cj+dk"
    * (without any brackets)
    */
   @Override
   public String toString() {
      String a = String.format("%.2f", real_part);
      String b = String.format("%.2fi", im_i);
      String c = String.format("%.2fj", im_j);
      String d = String.format("%.2fk", im_k);

      if (im_i > 0) {
         b = String.format("%.2fi", im_i);
      }
      if (im_j > 0) {
         c = String.format("%.2fj", im_j);
      }
      if (im_k > 0) {
         d = String.format("%.2fk", im_k);
      }

      System.out.println(a + b + c + d);
      return a + b + c + d;
   }

   /** Conversion from the string to the quaternion. 
    * Reverse to <code>toString</code> method.
    * @throws IllegalArgumentException if string s does not represent 
    *     a quaternion (defined by the <code>toString</code> method)
    * @param s string of form produced by the <code>toString</code> method
    * @return a quaternion represented by string s
    */
   public static Quaternion valueOf (String s) {
      Pattern pattern = Pattern.compile("(-?\\d+(?:\\.\\d+)?)([ijk])?");
      Matcher matcher = pattern.matcher(s);

      double a = 0.0, b = 0.0, c = 0.0, d = 0.0;
      while (matcher.find()) {
         double value = Double.parseDouble(matcher.group(1));
         String letter = matcher.group(2);
         if (letter == null) {
            a = value;
         } else if (letter.equals("i")) {
            b = value;
         } else if (letter.equals("j")) {
            c = value;
         } else if (letter.equals("k")) {
            d = value;
         }
      }
      return new Quaternion(a, b, c, d);
   }

   /** Clone of the quaternion.
    * @return independent clone of <code>this</code>
    */
   @Override
   public Object clone() throws CloneNotSupportedException {
      Quaternion cloned = new Quaternion(real_part, im_i, im_j, im_k);

      return cloned;
   }

   /** Test whether the quaternion is zero. 
    * @return true, if the real part and all the imaginary parts are (close to) zero
    */
   public boolean isZero() {
      if (Math.abs(real_part) < threshold && Math.abs(im_j) < threshold&& Math.abs(im_i) < threshold && Math.abs(im_k) < threshold){
         return true;
      }
      else {
         return false;
      }
   }

   /** Conjugate of the quaternion. Expressed by the formula 
    *     conjugate(a+bi+cj+dk) = a-bi-cj-dk
    * @return conjugate of <code>this</code>
    */
   public Quaternion conjugate() {
      Quaternion quat = new Quaternion(real_part, -im_i, -im_j, -im_k);

      return quat;
   }

   /** Opposite of the quaternion. Expressed by the formula 
    *    opposite(a+bi+cj+dk) = -a-bi-cj-dk
    * @return quaternion <code>-this</code>
    */
   public Quaternion opposite() {
      Quaternion quat = new Quaternion(-real_part, -im_i, -im_j, -im_k);

      return quat;
   }

   /** Sum of quaternions. Expressed by the formula 
    *    (a1+b1i+c1j+d1k) + (a2+b2i+c2j+d2k) = (a1+a2) + (b1+b2)i + (c1+c2)j + (d1+d2)k
    * @param q addend
    * @return quaternion <code>this+q</code>
    */
   public Quaternion plus (Quaternion q) {
      Quaternion quat = new Quaternion(real_part + q.real_part, im_i + q.im_i, im_j+ q.im_j, im_k+ q.im_k);

      return quat;
   }

   /** Product of quaternions. Expressed by the formula
    *  (a1+b1i+c1j+d1k) * (a2+b2i+c2j+d2k) = (a1a2-b1b2-c1c2-d1d2) + (a1b2+b1a2+c1d2-d1c2)i +
    *  (a1c2-b1d2+c1a2+d1b2)j + (a1d2+b1c2-c1b2+d1a2)k
    * @param q factor
    * @return quaternion <code>this*q</code>
    */
   public Quaternion times (Quaternion q) {
      double r = real_part*q.real_part - im_i*q.im_i - im_j*q.im_j - im_k*q.im_k;
      double i = real_part*q.im_i + im_i*q.real_part + im_j*q.im_k - im_k*q.im_j;
      double j = real_part*q.im_j - im_i*q.im_k + im_j*q.real_part + im_k*q.im_i;
      double k = real_part*q.im_k + im_i*q.im_j - im_j*q.im_i + im_k*q.real_part;

      return new Quaternion(r, i, j, k);
   }

   /** Multiplication by a coefficient.
    * @param r coefficient
    * @return quaternion <code>this*r</code>
    */
   public Quaternion times (double r) {
      Quaternion quat = new Quaternion(real_part * r, im_i * r, im_j * r, im_k * r);

      return quat;
   }

   /** Inverse of the quaternion. Expressed by the formula
    *     1/(a+bi+cj+dk) = a/(a*a+b*b+c*c+d*d) + 
    *     ((-b)/(a*a+b*b+c*c+d*d))i + ((-c)/(a*a+b*b+c*c+d*d))j + ((-d)/(a*a+b*b+c*c+d*d))k
    * @return quaternion <code>1/this</code>
    */
   public Quaternion inverse() {
      double num = real_part*real_part + im_i*im_i + im_j*im_j + im_k*im_k;
      if (num != 0.0){
         return new Quaternion(real_part*(1.0/num), -im_i*(1.0/num), -im_j*(1.0/num), -im_k*(1.0/num));
      }
      else {
         throw new ArithmeticException("Square of quaternion equals 0!");
      }
   }

   /** Difference of quaternions. Expressed as addition to the opposite.
    * @param q subtrahend
    * @return quaternion <code>this-q</code>
    */
   public Quaternion minus (Quaternion q) {
      Quaternion quat = new Quaternion(real_part - q.real_part, im_i - q.im_i, im_j - q.im_j, im_k - q.im_k);

      return quat;
   }


   public Quaternion divideByRight (Quaternion q) {
      return times(q.inverse());
   }

   // Because 5 / 2 == 5 * 1/2 (it was pretty hard for me)
   public Quaternion divideByLeft (Quaternion q) {
      return q.inverse().times(this);
   }
   

   @Override
   public boolean equals (Object qo) {
      if (!(qo instanceof Quaternion)) {
         return false;
      }
      else{
         if (this == qo){
            return true;
         }
         else{
            Quaternion a = (Quaternion) qo;
            boolean result = Math.abs(real_part - a.real_part) <= threshold && Math.abs(im_i - a.im_i) <= threshold && Math.abs(im_k - a.im_k) <= threshold && Math.abs(im_j - a.im_j) <= threshold;
            return  result;
         }
      }
   }

   //dotMult(p,q) = 1/2 * [(a+bi+cj+dk)(s−yi−zj−wk)+(s+yi+zj+wk)(a−bi−cj−dk)]
   public Quaternion dotMult (Quaternion q) {
      Quaternion conj_current = this.conjugate();
      Quaternion conj_q = q.conjugate();

      Quaternion first_part = this.times(conj_q);
      Quaternion second_part = q.times(conj_current);

      Quaternion result = first_part.plus(second_part).times(0.5);

      return result;
   }


   // https://www.digitalocean.com/community/tutorials/java-equals-hashcode
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 0;

      result = prime * result + Double.hashCode(im_i);
      result = prime * result + Double.hashCode(im_j);
      result = prime * result + Double.hashCode(im_k);
      result = prime * result + Double.hashCode(real_part);

      return result;
   }

   /** Norm of the quaternion. Expressed by the formula 
    *     norm(a+bi+cj+dk) = Math.sqrt(a*a+b*b+c*c+d*d)
    * @return norm of <code>this</code> (norm is a real number)
    */
   public double norm() {
      double result = Math.sqrt(real_part * real_part + im_i * im_i + im_j * im_j + im_k * im_k);

      return  result;
   }

   /** Main method for testing purposes. 
    * @param args command line parameters
    */
   public static void main (String[] args) {
      Quaternion q1 = new Quaternion(2, 2, 3, 4);
      Quaternion q2 = new Quaternion(2, 2, 3, 4);

      System.out.println(q1.norm());
      System.out.println(q1.hashCode());
      System.out.println(q2.hashCode());

      //Quaternion result = q1.dotMult(q2);
      //System.out.println(result.toString());
   }

}

