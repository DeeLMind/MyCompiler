package bit.minisys.minicc.scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.sun.corba.se.spi.orbutil.threadpool.Work;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class LexAnalyse1 {

	int index = 0;// ��ʼ״̬0���ַ��±�
	int line = 1;// ��ǰ����
	int len;// �ļ����ַ�����
	char[] str = new char[50000];// Դ�ļ�
	ArrayList<Token> TokenList = new ArrayList<Token>();// ���ʱ�
	ArrayList<ErrorWord> ErrorWordList = new ArrayList<ErrorWord>();// ������Ϣ�б�
	int TokenNum = 0;// ͳ�Ƶ��ʸ���
	int ErrorNum = 0;// ͳ�ƴ������
	Boolean ifError = false;
	Token token = null;
	ErrorWord errorword;
	// �ļ�IO
	File inFile;
	File outFile;
	Reader inFileReader;
	Writer outFileWriter;

	public LexAnalyse1(String iFile, String oFile) {

		// �������ļ���File ���ʾ
		inFile = new File(iFile);
		outFile = new File(oFile);
		// ��ʼ���ַ��� ��

		try {
			inFileReader = new FileReader(inFile);
			outFileWriter = new FileWriter(outFile);
			len = inFileReader.read(str);
			a0();
			// ����Ҫд�ļ�
			// =========================================
			outputXMLfile(outFile);
			// ==========================================
			outFileWriter.close();
			inFileReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	//���������Ϣ
	void error(String errorInfo, String tempStr) {
		ErrorNum++;
		ifError = true;
		// �����հ׷��žͻص�a0,��ʼ����µĴ�
		while (true) {
			if (index >= len)
				return;
			if (str[index] == ' ' || str[index] == '\t' || str[index] == '\r') {
				break;
			}
			tempStr+=str[index];
			index++;
		}
		TokenNum++;
		token = new Token();
		token.id = TokenNum;
		token.value = tempStr;
		token.type = Token.ERROR;
		token.line = line;
		token.flag = false;
		TokenList.add(token);
		ErrorWord error = new ErrorWord();
		error.id = ErrorNum;
		error.info = errorInfo;
		error.line = line;
		error.token = token; 
		ErrorWordList.add(error);
		a0();
	}

	// ���д����
	boolean if_change_line() {
		if (str[index] == '\r' && str[index + 1] == '\n') {
			line++;
			index++;
			index++;
			return true;
		}
		return false;
	}

	// ��ʼ״̬
	void a0() {
		if (index >= len)
			return;
		// �����հ׷��žͻص�a0
		while (str[index] == ' ' || str[index] == '\t'||str[index] == '\r') {
			//�Ƿ���
			if(if_change_line()){
				index--;
			}
			index++;
		}
		
		// ��ʶ����ؼ��֣�����״̬1
		if (('a' <= str[index] && str[index] <= 'z')
				|| ('A' <= str[index] && str[index] <= 'Z')
				|| str[index] == '_') {
			a1();
		}
		// {,},[,],(,),\,,;״̬2,�ָ���
		else if ('{' == str[index] || str[index] == '}' || str[index] == '['
				|| str[index] == ']' || str[index] == '(' || str[index] == ')'
				|| str[index] == ',' || str[index] == '.' || str[index] == ';') {
			a2();
		}
		// �ַ�����
		else if ('\'' == str[index]) {
			a3();
		}
		// �ַ�������
		else if ('\"' == str[index]) {
			a4();
		}
		// ���ֳ���,8 ���� 16���� ���μ�����������ѧ������
		else if (str[index] == '0') {
			a5();
		}
		// ���ֳ���,10���� ���μ�����������ѧ������
		else if ('1' <= str[index] && str[index] <= '9') {
			a6();
		}
		// +
		else if ('+' == str[index]) {
			a7();
		}
		// -
		else if ('-' == str[index]) {
			a8();
		}
		// *
		else if ('*' == str[index]) {
			a9();
		}
		// /
		else if ('/' == str[index]) {
			a10();
		}
		// %
		else if ('%' == str[index]) {
			a11();
		}
		// &
		else if ('&' == str[index]) {
			a12();
		}
		// =
		else if ('=' == str[index]) {
			a13();
		} else if ('<' == str[index]) {
			a14();
		} else if ('>' == str[index]) {
			a15();
		} else if ('|' == str[index]) {
			a16();
		} else if ('!' == str[index]) {
			a17();
		} else if ('^' == str[index]) {
			a18();
		} else if ('.' == str[index]) {
			a19();
		} else if ('?' == str[index]) {
			a20();
		} else {
			error("���ǺϷ����ַ�","");
		}

	}

	// ��ʶ���͹ؼ��֣�״̬1
	void a1() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		//if_change_line();
		while (('a' <= str[index] && str[index] <= 'z')
				|| ('A' <= str[index] && str[index] <= 'Z') || str[index] == '_'
				|| str[index] == '_'||('0' <= str[index] && str[index] <= '9')) {
			temp_str += str[index];
			index++;
			//if_change_line();
		}
		// ============================================
		TokenNum++;
		// �ж��Ƿ��ǹؼ���
		if (Token.isKey(temp_str)) {
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.KEY;
			token.line = line;
			TokenList.add(token);
		}
		// ��ʶ��
		else {
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.IDENTIFIER;
			token.line = line;
			TokenList.add(token);
		}
		// ===========================================
		a0();
	}

	// {,},[,],(,),\,,;״̬2,�ָ���,����״̬
	void a2() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		// ============================================
		TokenNum++;
		token = new Token();
		token.id = TokenNum;
		token.value = temp_str;
		token.type = Token.SEPARATOR;
		token.line = line;
		TokenList.add(token);
		// ===========================================
		a0();
	}

	// ״̬3 �ַ�����
	void a3() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		// ������\��'���ַ�
		if (str[index] != '\\' && str[index] != '\'') {
			temp_str += str[index];
			index++;
			// �����ţ��ַ�����
			if (str[index] == '\'') {
				temp_str += str[index];
				// ============================================
				TokenNum++;
				token = new Token();
				token.id = TokenNum;
				token.value = temp_str;
				token.type = Token.CONST_CHAR;
				token.line = line;
				TokenList.add(token);
				// ===========================================
				index++;
			} else {
				error("�ⲻ��һ���Ϸ����ַ�����",temp_str);
				
			}

		}// ת����ſ�ʼ
		else if (str[index] == '\\') {
			index++;
			// ת�� a��b��f��n,r,t,v,\,',",?,0
			if (str[index] == 'a' || str[index] == 'b' || str[index] == 'f'
					|| str[index] == 'n' || str[index] == 'r'
					|| str[index] == 't' || str[index] == 'v'
					|| str[index] == '\\' || str[index] == '\''
					|| str[index] == '\"' || str[index] == '?'
					|| str[index] == '0') {
				temp_str += str[index];
				index++;
				if (str[index] == '\'') {// �����Ž�β���ַ���������
					temp_str += str[index];
					index++;
					// ============================================
					TokenNum++;
					token = new Token();
					token.id = TokenNum;
					token.value = temp_str;
					token.type = Token.ESCAPE_CHAR;
					token.line = line;
					TokenList.add(token);
					// ============================================
				}

			} else {
				error("�ⲻ��һ���Ϸ���ת���ַ�����",temp_str);
				
			}
		} else {
			error("�ⲻ��һ���Ϸ����ַ�����",temp_str);
			
		}
		// ============================================

		// ===========================================
		a0();
	}

	// ״̬4 �ַ�������
	void a4() {

		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		while (true) {
			// �ַ����л��У�����
			if (if_change_line()) {
				error("�ַ����в�������",temp_str);
				break;
			}
			if (str[index] != '\\' && str[index] != '\"') {
				temp_str += str[index];
				index++;
			} else if (str[index] == '\\') {
				temp_str += str[index];
				index++;
				temp_str += str[index];
				index++;

			} else if (str[index] == '\"') {
				temp_str += str[index];
				index++;
				// ====================================
				TokenNum++;
				token = new Token();
				token.id = TokenNum;
				token.value = temp_str;
				token.type = Token.CONST_STR;
				token.line = line;
				TokenList.add(token);
				// ====================================
				// ��������
				break;
			}
			// �ַ���û��β
			if (index >= len) {
				error("ȱ��һ����β��\"",temp_str);
				break;
			}

		}
		a0();
	}

	// ���ֳ���,8 ���� 16���� ���μ�����������ѧ������
	void a5() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		// 0.xxxxxx�ĸ�����
		if (str[index] == '.') {
			temp_str += str[index];
			index++;
			while ('1' <= str[index] && str[index] <= '9') {
				temp_str += str[index];
				index++;
			}
			// ============================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.NUM_FLOAT;
			token.line = line;
			TokenList.add(token);
			// ============================================
		}
		// 8����
		else if ('0' <= str[index] && str[index] <= '7') {
			temp_str += str[index];
			index++;
			while ('0' <= str[index] && str[index] <= '7') {
				temp_str += str[index];
				index++;
			}
			
			// 8���Ƹ�����
			if (str[index] == '.' || str[index] == 'e' || str[index] == 'E') {
				temp_str += str[index];
				index++;
				if ('0' <= str[index] && str[index] <= '7') {
					temp_str += str[index];
					index++;
					while ('0' <= str[index] && str[index] <= '7') {
						temp_str += str[index];
						index++;
					}
					// 8���Ƹ���������
					// ==================================
					TokenNum++;
					token = new Token();
					token.id = TokenNum;
					token.value = temp_str;
					token.type = Token.NUM_FLOAT;
					token.line = line;
					TokenList.add(token);
					// =====================================
				} else {
					error("�ⲻ��һ���Ϸ��İ˽��Ƴ���",temp_str);
					
				}
			}
			else{
				// 8������������
				// ============================================
				TokenNum++;
				token = new Token();
				token.id = TokenNum;
				token.value = temp_str;
				token.type = Token.INT_8;
				token.line = line;
				TokenList.add(token);
				// ============================================
			}
		}
		// 16����
		else if (str[index] == 'x' || str[index] == 'X') {
			index++;
			if (('0' <= str[index] && str[index] <= '9')
					|| ('a' <= str[index] && str[index] <= 'f' || ('A' <= str[index] && str[index] <= 'F'))) {
				temp_str += str[index];
				index++;
				while (('0' <= str[index] && str[index] <= '9')
						|| ('a' <= str[index] && str[index] <= 'f' || ('A' <= str[index] && str[index] <= 'F'))) {
					temp_str += str[index];
					index++;
				}
				// 16����
				// ==================================
				TokenNum++;
				token = new Token();
				token.id = TokenNum;
				token.value = temp_str;
				token.type = Token.INT_16;
				token.line = line;
				TokenList.add(token);
				// =====================================
			} else {
				error("�ⲻ��һ���Ϸ���ʮ�����Ƴ���",temp_str);
				
			}
		}

		a0();
	}

	// ���ֳ���,10���� ���μ�����������ѧ������
	void a6() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		while ('0' <= str[index] && str[index] <= '9') {
			temp_str += str[index];
			index++;
		}
		if (str[index] == '.' || str[index] == 'e' || str[index] == 'E') {
			temp_str += str[index];
			index++;
			if ('0' <= str[index] && str[index] <= '9') {
				temp_str += str[index];
				index++;
				while ('0' <= str[index] && str[index] <= '9') {
					temp_str += str[index];
					index++;
				}
				// 10���Ƹ���������
				// ==================================
				TokenNum++;
				token = new Token();
				token.id = TokenNum;
				token.value = temp_str;
				token.type = Token.NUM_FLOAT;
				token.line = line;
				TokenList.add(token);
				// =====================================
			} else {
				error("�ⲻ��һ���Ϸ������ֳ���",temp_str);
				
			}
		}
		// �����ν���
		else if (str[index] == 'L') {
			temp_str += str[index];
			index++;
			// =========================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.INT_10_LONG;
			token.line = line;
			TokenList.add(token);
			// =========================================
		} else {
			// 10�������ν���
			// =============================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.INT_10;
			token.line = line;
			TokenList.add(token);
			// =============================================
		}

		a0();
	}

	// + ++ +=
	void a7() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		// ++
		if (str[index] == '+') {
			temp_str += str[index];
			index++;
			// ++����
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
		}
		// +=
		else if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// +=����
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
		} else {
			// +����
			// ===========================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ===========================================
		}
		a0();
	}

	// - -- -=
	void a8() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		// ++
		if (str[index] == '-') {
			temp_str += str[index];
			index++;
			// ++����
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
		}
		// +=
		else if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// +=����
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
			
		} else {
			// -����
			// ===========================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ===========================================
		}
		a0();
	}

	// * *=
	void a9() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		// *=
		if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// +=����
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
			
		} else {
			// *����
			// ===========================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ===========================================
		}
		a0();
	}

	// / /=
	void a10() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		// /=
		if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// +=����
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
			return;
		} else {
			// /����
			// ===========================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ===========================================
		}
		a0();
	}

	// % %=
	void a11() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		// %=
		if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// %=����
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
		} else {
			// %����
			// ===========================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ===========================================
		}
		a0();
	}

	// & && &=
	void a12() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		// &&
		if (str[index] == '&') {
			temp_str += str[index];
			index++;
			// &&����
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
			return;
		}
		// &=
		else if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// &=����
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
		} else {
			// &����
			// ===========================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ===========================================
		}
		a0();
	}

	// = ==
	void a13() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		// ==
		if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// ==����
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
		} else {
			// =����
			// ===========================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ===========================================
		}
		a0();

	}

	// < << <= <<=
	void a14() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		// <<
		if (str[index] == '<') {
			temp_str += str[index];
			index++;
			// <<=
			if (str[index] == '=') {
				temp_str += str[index];
				index++;
				// <<=����
				// =======================================
				TokenNum++;
				token = new Token();
				token.id = TokenNum;
				token.value = temp_str;
				token.type = Token.OPERATOR;
				token.line = line;
				TokenList.add(token);
				// ==========================================
			}
			// <<����
			// =======================================

			// ==========================================
		
		}
		// <=
		else if (str[index] == '=') {
			temp_str += str[index];
			index++;

			// <=����
			// =======================================

			// ==========================================
		
		} else {
			// &����
			// ===========================================

			// ===========================================
		}
		a0();
	}

	// > >> >= >>=
	void a15() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		// >>
		if (str[index] == '>') {
			temp_str += str[index];
			index++;
			// >>
			if (str[index] == '=') {
				temp_str += str[index];
				index++;
				// >>=����
				// =======================================
				TokenNum++;
				token = new Token();
				token.id = TokenNum;
				token.value = temp_str;
				token.type = Token.OPERATOR;
				token.line = line;
				TokenList.add(token);
				// ==========================================
				
			}
			// <<����
			// =======================================

			// ==========================================
			
		}
		// >=
		else if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// <<=

			// <=����
			// =======================================

			// ==========================================
			
		} else {
			// &����
			// ===========================================

			// ===========================================
		}
		a0();
	}

	// | || |=
	void a16() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		// ||
		if (str[index] == '|') {
			temp_str += str[index];
			index++;
			// &&����
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
			
		}
		// |=
		else if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// |=����
			// =======================================

			// ==========================================
			
		} else {
			// &����
			// ===========================================

			// ===========================================
		}
		a0();
	}

	// ! !=
	void a17() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		// !=
		if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// !=����
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
			
		} else {
			// !����
			// ===========================================

			// ===========================================
		}
		a0();
	}

	// ^ ^=
	void a18() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		// !=
		if (str[index] == '=') {
			temp_str += str[index];
			index++;
			// ^=����
			// =======================================
			TokenNum++;
			token = new Token();
			token.id = TokenNum;
			token.value = temp_str;
			token.type = Token.OPERATOR;
			token.line = line;
			TokenList.add(token);
			// ==========================================
			
		} else {
			// ^����
			// ===========================================

			// ===========================================
		}
		a0();
	}

	// .
	void a19() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		// ====================================
		TokenNum++;
		token = new Token();
		token.id = TokenNum;
		token.value = temp_str;
		token.type = Token.OPERATOR;
		token.line = line;
		TokenList.add(token);
		// ====================================
		a0();
	}

	// ?
	void a20() {
		if (index >= len)
			return;
		// ��ʱ�ַ�����
		String temp_str = "";
		temp_str += str[index];
		index++;
		// ====================================
		TokenNum++;
		token = new Token();
		token.id = TokenNum;
		token.value = temp_str;
		token.type = Token.OPERATOR;
		token.line = line;
		TokenList.add(token);
		// ====================================
		a0();
	}
	//�����Ϣ
	public String outputXMLfile(File file) throws IOException {
		//File file = new File(".\\input\\");
		if (!file.exists()) {// �������ļ������ھʹ�����
			file.mkdirs();
			file.createNewFile();
		}
		String path = file.getAbsolutePath();

		Element root = new Element("project").setAttribute("name", "test.token.xml");

		Document Doc = new Document(root);
		//��ʼ��ǩ
		Element tokens = new Element("tokens");
		root.addContent(tokens);
		for (int i = 0; i < this.TokenList.size(); i++) {
			Token word = (Token) this.TokenList.get(i);

			Element elements = new Element("token");
			elements.addContent(new Element("number").setText(new Integer(
					word.id).toString()));
			elements.addContent(new Element("value").setText(word.value));
			elements.addContent(new Element("type").setText(word.type));
			elements.addContent(new Element("line").setText(new Integer(
					word.line).toString()));
			elements.addContent(new Element("valid").setText(new Boolean(
					word.flag).toString()));

			tokens.addContent(elements);
		}
		if (ifError) {
			for (int i = 0; i < ErrorWordList.size(); i++) {
				ErrorWord error = (ErrorWord) this.ErrorWordList.get(i);

				Element elements = new Element("������Ϣ");

				elements.addContent(new Element("������").setText(new Integer(
						error.id).toString()));
				elements.addContent(new Element("������Ϣ").setText(error.info));
				elements.addContent(new Element("����������").setText(new Integer(
						error.line).toString()));
				elements.addContent(new Element("���󵥴�")
						.setText(error.token.value));

				root.addContent(elements);
			}
		}
		Format format = Format.getPrettyFormat();
		XMLOutputter XMLOut = new XMLOutputter(format);
		XMLOut.output(Doc, new FileOutputStream(path));

		return path;
	}
}
