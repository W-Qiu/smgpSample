package com.wondertek.ctmp.protocol.util;

/**
 * @author chensheng
 */
import java.util.Date;
import java.util.Random;

/***********************************************************************
 <P>
 Return a random int, long, double, or String of positive integer

 @ NOte: The random number distribution is NOT uniform
 @author
 ************************************************************************/
public class RandomGenerator {
	private static int BYTE_MAX_LEN = -1;

	private static int SHORT_MAX_LEN = -1;

	private static int INT_MAX_LEN = -1;

	private static int LONG_MAX_LEN = -1;

	// these are set temporarily
	private final static int FLOAT_MAX_LEN = 8;

	private final static int DOUBLE_MAX_LEN = 16;

	// this random instance is used by all static non-repeatable methods
	private static Random mRandom = new Random(new Date().getTime());

	// re-seed frequency of mRandom
	private final static int RESEED_FREQUENCY = 30;

	// usage counter of mRandom
	private static int counter = 0;

	private Random mRandBoolean; // for repeatable default length boolean

	private Random mRandByte; // for repeatable default length byte

	private Random mRandChar; // for repeatable default length char

	private Random mRandShort; // for repeatable default length short

	private Random mRandInt; // for repeatable default length int

	private Random mRandLong; // for repeatable default length long

	private Random mRandFloat; // for repeatable default length float

	private Random mRandDouble; // for repeatable default length double

	private Random mRandString; // for repeatable default length String

	private Random mRandAbsShort; // for repeatable default length abs short

	private Random mRandAbsInt; // for repeatable default length abs int

	private Random mRandAbsLong; // for repeatable default length abs long

	private Random mRandAbsFloat; // for repeatable default length abs float

	private Random mRandAbsDouble; // for repeatable default length abs double

	private Random lRandByte; // for repeatable fixed length byte

	private Random lRandShort; // for repeatable fixed length short

	private Random lRandInt; // for repeatable fixed length int

	private Random lRandLong; // for repeatable fixed length long

	private Random lRandFloat; // for repeatable fixed length float

	private Random lRandDouble; // for repeatable fixed length double

	private Random lRandString; // for repeatable fixed length String

	private Random lRandAbsShort; // for repeatable fixed length short

	private Random lRandAbsInt; // for repeatable fixed length int

	private Random lRandAbsLong; // for repeatable fixed length long

	private Random lRandAbsFloat; // for repeatable fixed length float

	private Random lRandAbsDouble; // for repeatable fixed length double

	// --- Constructors ----------------------------------//
	public RandomGenerator() {
		seed(603563);
	}

	public RandomGenerator(long s) {
		seed(s);
	}

	public void seed(long s) {
		mRandByte = new Random(s);
		mRandChar = new Random(s + 1931);
		mRandShort = new Random(s + 2777);
		mRandInt = new Random(s + 3089);
		mRandLong = new Random(s + 3373);
		mRandFloat = new Random(s + 10259);
		mRandDouble = new Random(s + 97169);
		mRandString = new Random(s + 266177);
		mRandAbsShort = new Random(s + 26209);
		mRandAbsInt = new Random(s + 1237);
		mRandAbsLong = new Random(s + 10903);
		mRandAbsFloat = new Random(s + 18013);
		mRandAbsDouble = new Random(s + 27743);
		mRandBoolean = new Random(s + 167);

		lRandByte = new Random(s + 2143);
		lRandShort = new Random(s + 31663);
		lRandInt = new Random(s + 12329);
		lRandLong = new Random(s + 35221);
		lRandFloat = new Random(s + 67567);
		lRandDouble = new Random(s + 65027);
		lRandString = new Random(s + 159499);
		lRandAbsShort = new Random(s + 3331);
		lRandAbsInt = new Random(s + 45341);
		lRandAbsLong = new Random(s + 16447);
		lRandAbsFloat = new Random(s + 103903);
		lRandAbsDouble = new Random(s + 24107);
	}

	private static void reSeed() {
		if (counter > RESEED_FREQUENCY) {
			counter = 0;

			long x = new Date().getTime() - Math.abs(mRandom.nextInt());
			mRandom.setSeed(x);
		}

		++counter;
	}

	// ----- boolean ------------------------------------------------- //
	// repeatable random value,
	// (same sequence for any instance with the same seed)
	public boolean nextBoolean() {
		if (1 == (mRandBoolean.nextInt() & 1)) {
			return true;
		} else {
			return false;
		}
	}

	// non-repeatable random value
	public static boolean getBoolean() {
		reSeed();

		if (1 == (mRandom.nextInt() & 1)) {
			return true;
		} else {
			return false;
		}
	}

	// ----- byte ------------------------------------------------- //
	// repeatable random value,
	// (same sequence for any instance with the same seed)
	public byte nextByte() {
		byte[] b = new byte[1];
		mRandByte.nextBytes(b);

		return b[0];
	}

	// repeatable random value,
	// (same sequence for any instance with the same seed)
	// if len < 0 use default
	public byte nextByte(int len) {
		if (len < 0) {
			byte[] b = new byte[1];
			lRandByte.nextBytes(b);

			return b[0];
		}

		return ranByte(len, lRandByte);
	}

	// non-repeatable random value
	public static byte getByte() {
		byte[] b = new byte[1];
		reSeed();
		mRandom.nextBytes(b);

		return b[0];
	}

	// non-repeatable random value with fixed length
	// if len < 0 use default
	public static byte getByte(int len) {
		if (len < 0) {
			return getByte();
		}

		return ranByte(len, mRandom);
	}

	// repeatable random value with fixed length
	// (same sequence for any instance with the same seed)
	private static synchronized byte ranByte(int len, Random ran) {
		int max = maxByteLen();
		len = (len > max) ? max : len;
		len = (len > 0) ? len : 1;

		// make sure it is not out of range
		while (true) {
			try {
				return new Byte(findNextString(len, ran)).byteValue();
			} catch (NumberFormatException e) {}
		}
	}

	// ----- char ------------------------------------------------- //
	// printable char 33 - 126
	// repeatable random value, length is always 1
	// (same sequence for any instance with the same seed)
	public char nextChar() {
		return (char) ((Math.abs(mRandChar.nextInt()) % 94) + 33);
	}

	// non-repeatable random value, length is always 1
	// printable char 33 - 126
	public static char getChar() {
		reSeed();

		return (char) ((Math.abs(mRandom.nextInt()) % 94) + 33);
	}

	// ----- short ------------------------------------------------- //
	// repeatable random value,
	// (same sequence for any instance with the same seed)
	public short nextShort() {
		return (short) (mRandShort.nextInt() % Short.MAX_VALUE);
	}

	// repeatable random value,
	// (same sequence for any instance with the same seed)
	public short nextAbsShort() {
		return (short) Math.abs((mRandAbsShort.nextInt() % Short.MAX_VALUE));
	}

	// repeatable random value with fixed length
	// (same sequence for any instance with the same seed)
	// if len < 0 use default
	public short nextShort(int len) {
		if (len < 0) {
			return (short) (lRandShort.nextInt() % Short.MAX_VALUE);
		}

		return ranShort(len, lRandShort, true);
	}

	// repeatable random value with fixed length
	// (same sequence for any instance with the same seed)
	// if len < 0 use default
	public short nextAbsShort(int len) {
		if (len < 0) {
			return (short) Math.abs((lRandAbsShort.nextInt() % Short.MAX_VALUE));
		}

		return ranShort(len, lRandAbsShort, false);
	}

	// non-repeatable random value
	public static short getShort() {
		reSeed();

		return (short) (mRandom.nextInt() % Short.MAX_VALUE);
	}

	// non-repeatable random value
	public static short getAbsShort() {
		reSeed();

		return (short) Math.abs((mRandom.nextInt() % Short.MAX_VALUE));
	}

	// non-repeatable random value with fixed length
	// if len < 0 use default
	public static short getShort(int len) {
		reSeed();

		if (len < 0) {
			return getShort();
		}

		return ranShort(len, mRandom, true);
	}

	// non-repeatable random value with fixed length
	// if len < 0 use default
	public static short getAbsShort(int len) {
		reSeed();

		if (len < 0) {
			return getAbsShort();
		}

		return ranShort(len, mRandom, false);
	}

	// random value with fixed length
	private static synchronized short ranShort(int len, Random ran, boolean signed) {
		int max = maxShortLen();
		len = (len > max) ? max : len;
		len = (len > 0) ? len : 1;

		// make sure it is not out of range
		while (true) {
			try {
				if (signed && (ran.nextFloat() < 0.5)) {
					return (short) (-new Short(findNextString(len, ran)).shortValue());
				}

				return new Short(findNextString(len, ran)).shortValue();
			} catch (NumberFormatException e) {}
		}
	}

	// ----- int ------------------------------------------------- //
	// repeatable random value,
	// (same sequence for any instance with the same seed)
	public int nextInt() {
		return mRandInt.nextInt();
	}

	// repeatable random value,
	// (same sequence for any instance with the same seed)
	public int nextAbsInt() {
		return Math.abs(mRandAbsInt.nextInt());
	}

	// repeatable random value with fixed length
	// (same sequence for any instance with the same seed)
	// if len < 0 use default
	public int nextInt(int len) {
		if (len < 0) {
			return lRandInt.nextInt();
		}

		return ranInt(len, lRandInt, true);
	}

	// repeatable random value with fixed length
	// (same sequence for any instance with the same seed)
	// if len < 0 use default
	public int nextAbsInt(int len) {
		if (len < 0) {
			return Math.abs(lRandAbsInt.nextInt());
		}

		return ranInt(len, lRandAbsInt, false);
	}

	// non-repeatable random value
	public static int getInt() {
		reSeed();

		return mRandom.nextInt();
	}

	// non-repeatable random value
	public static int getAbsInt() {
		reSeed();

		return Math.abs(mRandom.nextInt());
	}

	// non-repeatable random value with fixed length
	// if len < 0 use default
	public static int getInt(int len) {
		reSeed();

		if (len < 0) {
			return getInt();
		}

		return ranInt(len, mRandom, true);
	}

	// non-repeatable random value with fixed length
	// if len < 0 use default
	public static int getAbsInt(int len) {
		reSeed();

		if (len < 0) {
			return getAbsInt();
		}

		return ranInt(len, mRandom, false);
	}

	//  random value with fixed length
	// if len < 0 use default
	private static synchronized int ranInt(int len, Random ran, boolean signed) {
		int max = maxIntLen();
		len = (len > max) ? max : len;
		len = (len > 0) ? len : 1;

		// make sure it is not out of range
		while (true) {
			try {
				if (signed && (ran.nextFloat() < 0.5)) {
					return -new Integer(findNextString(len, ran)).intValue();
				}

				return new Integer(findNextString(len, ran)).intValue();
			} catch (NumberFormatException e) {}
		}
	}

	// ------- long ----------------------------------------------- //
	// repeatable random value,
	// (same sequence for any instance with the same seed)
	public long nextLong() {
		return mRandLong.nextLong();
	}

	// repeatable random value,
	// (same sequence for any instance with the same seed)
	public long nextAbsLong() {
		return Math.abs(mRandAbsLong.nextLong());
	}

	// repeatable random value with fixed length
	// (same sequence for any instance with the same seed)
	// if len < 0 use default
	public long nextLong(int len) {
		if (len < 0) {
			return lRandLong.nextLong();
		}

		return ranLong(len, lRandLong, true);
	}

	// repeatable random value with fixed length
	// (same sequence for any instance with the same seed)
	// if len < 0 use default
	public long nextAbsLong(int len) {
		if (len < 0) {
			return Math.abs(lRandAbsLong.nextLong());
		}

		return ranLong(len, lRandAbsLong, false);
	}

	// non-repeatable random value
	public static long getLong() {
		reSeed();

		return mRandom.nextLong();
	}

	// non-repeatable random value
	public static long getAbsLong() {
		reSeed();

		return Math.abs(mRandom.nextLong());
	}

	// non-repeatable random value with fixed length
	public static long getLong(int len) {
		reSeed();

		if (len < 0) {
			return mRandom.nextLong();
		}

		return ranLong(len, mRandom, true);
	}

	// non-repeatable random value with fixed length
	// if len < 0 use default
	public static long getAbsLong(int len) {
		reSeed();

		if (len < 0) {
			return Math.abs(mRandom.nextLong());
		}

		return ranLong(len, mRandom, false);
	}

	// random value with fixed length
	// if len < 0 use default
	private static synchronized long ranLong(int len, Random ran, boolean signed) {
		int max = maxLongLen();
		len = (len > max) ? max : len;
		len = (len > 0) ? len : 1;

		// make sure it is not out of range
		while (true) {
			try {
				if (signed && (ran.nextFloat() < 0.5)) {
					return -new Long(findNextString(len, ran)).longValue();
				}

				return new Long(findNextString(len, ran)).longValue();
			} catch (NumberFormatException e) {}
		}
	}

	// -------- float ---------------------------------------------- //
	// repeatable random value,
	// (same sequence for any instance with the same seed)
	public float nextFloat() {
		return mRandFloat.nextFloat();
	}

	// repeatable random value,
	// (same sequence for any instance with the same seed)
	public float nextAbsFloat() {
		return Math.abs(mRandAbsFloat.nextFloat());
	}

	// repeatable random value with fixed length
	// (same sequence for any instance with the same seed)
	public float nextFloat(int len) {
		if (len < 0) {
			return lRandFloat.nextFloat();
		}

		return ranFloat(len, lRandAbsFloat, true);
	}

	// repeatable random value with fixed length
	// (same sequence for any instance with the same seed)
	// if len < 0 use default
	public float nextAbsFloat(int len) {
		if (len < 0) {
			return Math.abs(lRandAbsFloat.nextFloat());
		}

		return ranFloat(len, lRandAbsFloat, false);
	}

	// non-repeatable random value
	public static float getFloat() {
		reSeed();

		return mRandom.nextFloat();
	}

	// non-repeatable random value
	public static float getAbsFloat() {
		reSeed();

		return Math.abs(mRandom.nextFloat());
	}

	// non-repeatable random value with fixed length
	// if len < 0 use default
	public static float getFloat(int len) {
		reSeed();

		if (len < 0) {
			return getFloat();
		}

		return ranFloat(len, mRandom, true);
	}

	// non-repeatable random value with fixed length
	// if len < 0 use default
	public static float getAbsFloat(int len) {
		reSeed();

		if (len < 0) {
			return getAbsFloat();
		}

		return ranFloat(len, mRandom, false);
	}

	// random value with fixed length
	private static synchronized float ranFloat(int len, Random ran, boolean signed) {
		int max = maxFloatLen();
		len = (len > max) ? max : len;
		len = (len > 0) ? len : 1;

		// make sure it is not out of range
		while (true) {
			try {
				if (signed && (ran.nextFloat() < 0.5)) {
					return -new Float(findNextString(len, ran)).floatValue();
				}

				return new Float(findNextString(len, ran)).floatValue();
			} catch (NumberFormatException e) {}
		}
	}

	// ------  double  ------------------------------------------------ //
	// repeatable random value,
	// (same sequence for any instance with the same seed)
	public double nextDouble() {
		return mRandDouble.nextDouble();
	}

	// repeatable random value,
	// (same sequence for any instance with the same seed)
	public double nextAbsDouble() {
		return Math.abs(mRandAbsDouble.nextDouble());
	}

	// repeatable random value with fixed length
	// (same sequence for any instance with the same seed)
	// if len < 0 use default
	public double nextDouble(int len) {
		if (len < 0) {
			return lRandDouble.nextDouble();
		}

		return ranDouble(len, lRandDouble, true);
	}

	// repeatable random value with fixed length
	// (same sequence for any instance with the same seed)
	// if len < 0 use default
	public double nextAbsDouble(int len) {
		if (len < 0) {
			return Math.abs(lRandAbsDouble.nextDouble());
		}

		return ranDouble(len, lRandAbsDouble, false);
	}

	// non-repeatable random value
	public static double getDouble() {
		reSeed();

		return mRandom.nextDouble();
	}

	// non-repeatable random value
	public static double getAbsDouble() {
		reSeed();

		return Math.abs(mRandom.nextDouble());
	}

	// non-repeatable random value with fixed length
	// if len < 0 use default
	public static double getDouble(int len) {
		reSeed();

		if (len < 0) {
			return getDouble();
		}

		return ranDouble(len, mRandom, true);
	}

	// non-repeatable random value with fixed length
	// if len < 0 use default
	public static double getAbsDouble(int len) {
		reSeed();

		if (len < 0) {
			return getAbsDouble();
		}

		return ranDouble(len, mRandom, false);
	}

	// repeatable random value with fixed length
	// (same sequence for any instance with the same seed)
	private static synchronized double ranDouble(int len, Random ran, boolean signed) {
		int max = maxDoubleLen();
		len = (len > max) ? max : len;
		len = (len > 0) ? len : 1;

		// make sure it is not out of range
		while (true) {
			try {
				if (signed && (ran.nextFloat() < 0.5)) {
					return -new Double(findNextString(len, ran)).doubleValue();
				}

				return new Double(findNextString(len, ran)).doubleValue();
			} catch (NumberFormatException e) {}
		}
	}

	// ------- String  ----------------------------------------------- //
	// repeatable random value,
	// (same sequence for any instance with the same seed)
	public String nextString() {
		// default is 10
		return findNextString(10, mRandString);
	}

	// repeatable random value,
	// (same sequence for any instance with the same seed)
	// if len < 0 use default
	public String nextString(int len) {
		if (len < 0) {
			return findNextString(10, lRandString);
		}

		return findNextString(len, lRandString);
	}

	// non-repeatable random value
	public static String getString() {
		reSeed();

		// default is 10 digits
		return findNextString(10, mRandom);
	}

	// non-repeatable random value
	// if len < 0 use default
	public static String getString(int len) {
		reSeed();

		if (len == 0) {
			return "";
		}

		if (len < 0) {
			return getString();
		}

		return findNextString(len, mRandom);
	}

	// ---------------------------------------------- //
	private static int maxByteLen() {
		if (BYTE_MAX_LEN == -1) {
			BYTE_MAX_LEN = (new Byte(Byte.MAX_VALUE)).toString().length();
		}

		return BYTE_MAX_LEN;
	}

	private static int maxShortLen() {
		if (SHORT_MAX_LEN == -1) {
			SHORT_MAX_LEN = (new Short(Short.MAX_VALUE)).toString().length();
		}

		return SHORT_MAX_LEN;
	}

	private static int maxIntLen() {
		if (INT_MAX_LEN == -1) {
			INT_MAX_LEN = (new Integer(Integer.MAX_VALUE)).toString().length();
		}

		return INT_MAX_LEN;
	}

	private static int maxLongLen() {
		if (LONG_MAX_LEN == -1) {
			LONG_MAX_LEN = (new Long(Long.MAX_VALUE)).toString().length();
		}

		return LONG_MAX_LEN;
	}

	private static int maxFloatLen() {
		return FLOAT_MAX_LEN;
	}

	private static int maxDoubleLen() {
		return DOUBLE_MAX_LEN;
	}

	// random value of fixed length for String
	private static synchronized String findNextString(int len, Random ran) {
		int iLen = len;

		// min is 1
		iLen = (iLen > 0) ? iLen : 1;

		StringBuffer sb = new StringBuffer();

		// get a non-zero leading digit
		int x = 1 + (Math.abs(ran.nextInt()) % 9); // range from 1 to 9
		sb.append(x);
		iLen--;

		// get the rest
		while (iLen > 5) {
			sb.append(Math.abs(ran.nextLong()));
			iLen = len - sb.length();
		}

		// check if it is longer than we need
		if (sb.length() > len) {
			try {
				return sb.toString().substring(0, len);
			} catch (StringIndexOutOfBoundsException e) {}
			// never happen
		}

		// handle the last five or less
		while (iLen > 0) {
			// add 1 digit
			sb.append(Math.abs(ran.nextInt() % 10));
			iLen--;
		}

		return sb.toString();
	}

}
