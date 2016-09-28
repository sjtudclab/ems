package org.dclab.common;

public class Constants {
	public static final byte SINGLE_CHOICE	=	0;
	public static final byte MULTI_CHOICES	=	1;
	public static final byte JUDGEMENT		=	2;
	public static final byte MATCHING		=	3;
	public static final byte SHORT_ANSWER	=	4;
	public static final byte FILL_BLANK		=	5;
	public static final byte MACHINE_TEST	=	6;
	public static int JUDGEMENT_TRUE  =   61;
	public static int JUDGEMENT_FALSE =   62;
	public static String photoDir = new String();
	public static String multiMediaDir = new String();
	public static volatile boolean CanGetRoomInfo = false;
	public static volatile boolean superLoginFlag = false;
}
