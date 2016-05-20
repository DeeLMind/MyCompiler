package bit.minisys.minicc.pp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class PreProcessor  {
	File inFile;
	File outFile;
	Reader inFileReader;
	Writer outFileWriter;
	// numInFile ͳ�������ļ��е��ַ��ĸ���
	int numInFile;
	// ����ַ�������
	char buffer[] = new char[20000];
	// ������±�
	int i = 0;
	//�������е����⴦��
	int if_can_solve = 0;
	//���׵Ŀո���
	int if_line_statr = 0;
	String Out = new String();
	
	public void run(String iFile, String oFile) {
		// TODO Auto-generated method stub
		// �������ļ���File ���ʾ
		inFile = new File(iFile);
		outFile = new File(oFile);
		// ��ʼ���ַ��� ��
		try {
			inFileReader = new FileReader(inFile);
			outFileWriter = new FileWriter(outFile);
			numInFile = inFileReader.read(buffer);
			preprocess();
			outFileWriter.write(Out);
			outFileWriter.close();
			inFileReader.close();
			System.out.println("1. Ԥ�������");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Ԥ����ʼ
	void preprocess() {

		while (true) {
			if_can_solve = 0;
			if (i >= numInFile) {
				break;
			}
			// ����ǿո�,tab���߻س�
			if (buffer[i] == ' ' || buffer[i] == '\t' || buffer[i] == '\r') {
				skip_white_space();
			} else if (buffer[i] == '/') {
				//�ǿո�
				if_line_statr=0;
				// �鿴��һ���ַ��ǲ���ע�͵Ŀ�ʼ����
				if (buffer[i + 1] == '*') {
					i++;// ��ע��/*�Ŀ�ʼ����
					parse_comment();
				}
				if (buffer[i + 1] == '/') {
					i++;// ��//ע��
					// �������س�������ʧЧ
					while (true) {
						i++;
						if (buffer[i] == '\r') {
							i++;
							if (buffer[i] == '\n') {
								if_can_solve=1;
								if_line_statr=1;
								Out+="\r"+"\n";
							}
							break;
						}
					}
				}
			}

			if(if_can_solve!=1){
				if ((buffer[i] == ' ' || buffer[i] == '\t' 
						|| buffer[i] == '\r')&&if_line_statr==1) {
					//����ǿո񣬲����ǿ�ͷ
				}else{
					// ���������ַ��洢
					Out += buffer[i];
					if_line_statr=0;
				}
			}

			i++;
		}
	}

	/*
	 * ɾ��ע��
	 */
	void parse_comment() {
		i++;
		while (true) {
			if (i >= numInFile) {
				break;
			}
			// ע�����
			if (buffer[i] == '*' && buffer[i + 1] == '/') {
				i++;
				if_can_solve=1;
				return;
			}
			if (buffer[i] == '\r') {
				// �Ƿ��ǻ���,�������е�����һ��
				if (buffer[i+1] == '\n') {
					i++;
					if_can_solve=1;
					if_line_statr=1;
					Out+="\r"+"\n";
					}
				}
			i++;
		}
	}

	/*
	 * ���Կհ׷�
	 */
	void skip_white_space() {
		// ���Կո�Tab�ͻس�
		while (buffer[i] == ' ' || buffer[i] == '\t' || buffer[i] == '\r') {
			if (i >= numInFile) {
				break;
			}
			// '/r' �ǵ����ף�'\n'�ǵ���һ��
			if (buffer[i] == '\r') {
				// �Ƿ��ǻ���,�������е�����һ��
				if (buffer[i+1] == '\n') {
					i++;
					if_can_solve=1;
					if_line_statr=1;
					Out+="\r"+"\n";
					return;
				}

			}
			i++;
		}
		//����������ף���ô���ʼ䱣��һ���ո�
		i--;
	}

}
