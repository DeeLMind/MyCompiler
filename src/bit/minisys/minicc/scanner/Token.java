package bit.minisys.minicc.scanner;

import java.util.ArrayList;

/*
 * ��ŵ��ʵ���
 * 
 * */
public class Token {
	public final static String KEY = "�ؼ���";
	public final static String IDENTIFIER = "��ʶ��";
	public final static String SEPARATOR = "�ָ���";
	public final static String OPERATOR = "�����";
	//����
	public final static String CONST_CHAR = "�ַ�����";
	public final static String ESCAPE_CHAR = "ת���ַ�����";
	public final static String CONST_STR = "�ַ�������";
	public final static String INT_16 = "ʮ����������";
	public final static String INT_8 = "�˽�������";
	public final static String INT_10 = "ʮ��������";
	public final static String INT_10_LONG = "ʮ���Ƴ�����";
	public final static String NUM_FLOAT = "������";
	public final static String ERROR = "����";
	public static ArrayList<String> key = new ArrayList<String>();// �ؼ���
	public static ArrayList<String> seperator = new ArrayList<String>();// �ָ���
	public static ArrayList<String> operator = new ArrayList<String>();// �����
	
	int id;// �������
	String value;// ���ʵ�ֵ
	String type;// ��������
	int line;// ����������
	boolean flag = true;//�����Ƿ�Ϸ�
	
	static {
		Token.operator.add("+");
		Token.operator.add("++");
		Token.operator.add("+=");
		Token.operator.add("-");
		Token.operator.add("--");
		Token.operator.add("-=");
		Token.operator.add("*");
		Token.operator.add("*=");
		Token.operator.add("/");
		Token.operator.add("/=");
		Token.operator.add(">");
		Token.operator.add(">=");
		Token.operator.add(">>");
		Token.operator.add(">>=");
		Token.operator.add("<");
		Token.operator.add("<=");
		Token.operator.add("<<");
		Token.operator.add("<<=");
		Token.operator.add("=");
		Token.operator.add("==");
		Token.operator.add("!");
		Token.operator.add("!=");
		Token.operator.add("&");
		Token.operator.add("&=");
		Token.operator.add("&&");
		Token.operator.add("|");
		Token.operator.add("|=");
		Token.operator.add("||");
		Token.operator.add(".");
		Token.operator.add("?");
		
		
		Token.seperator.add("(");
		Token.seperator.add(")");
		Token.seperator.add("{");
		Token.seperator.add("}");
		Token.seperator.add("[");
		Token.seperator.add("]");
		Token.seperator.add(";");
		Token.seperator.add(",");
		
		Token.key.add("auto");
		Token.key.add("break");
		Token.key.add("case");
		Token.key.add("char");
		Token.key.add("const");
		Token.key.add("continue");
		Token.key.add("default");
		Token.key.add("do");
		Token.key.add("double");
		Token.key.add("else");
		Token.key.add("enum");
		Token.key.add("extern");
		Token.key.add("float");
		Token.key.add("for");
		Token.key.add("goto");
		Token.key.add("if");
		Token.key.add("int");
		Token.key.add("long");
		Token.key.add("register");
		Token.key.add("return");
		Token.key.add("short");
		Token.key.add("signed");
		Token.key.add("sizeof");
		Token.key.add("static");
		Token.key.add("struct");
		Token.key.add("swich");
		Token.key.add("typedef");
		Token.key.add("union");
		Token.key.add("unsigned");
		Token.key.add("void");
		Token.key.add("volatile");
		Token.key.add("while");
		Token.key.add("scanf");
		Token.key.add("printf");
	}
	
	
	public Token() {

	}

	public Token(int id, String value, String type, int line) {
		this.id = id;
		this.value = value;
		this.type = type;
		this.line = line;
	}

	public static boolean isKey(String word) {
		return key.contains(word);
	}

	public static boolean isOperator(String word) {
		return operator.contains(word);
	}

	public static boolean isSeperator(String word) {
		return seperator.contains(word);
	}
}
