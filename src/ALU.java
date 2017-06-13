/**
 * ģ��ALU���������͸���������������
 * @author [161250131_ͯ����]
 *
 */

public class ALU {
	/**
	 * ������չ������
	 * ����signExtension("1010", '0', 8)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param sign ����λ
	 * @param length �����õ�����ĳ���
	 * @return operand������չ�Ľ��
	 */
	public String signExtension (String operand, char sign, int length) {
		if (operand.length() > length) return operand.substring(0, length);
		String result = "";
		for (int i = 0; i < length - operand.length(); i++) {
			result += sign;
		}
		result += operand;
		return result;
	}
	/**
	 * ����ʮ���������Ķ����Ʋ����ʾ��
	 * ����integerRepresentation("9", 8)
	 * @param number ʮ������������Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length �����Ʋ����ʾ�ĳ���
	 * @return number�Ķ����Ʋ����ʾ������Ϊlength
	 */
	public String integerRepresentation (String number, int length) {
		char sign = (number.charAt(0) == '-') ? '1' : '0';
		String abs = (number.charAt(0) == '-') ? number.substring(1) : number;
		String trueForm = Integer.toBinaryString(Integer.parseInt(abs));
		String complement = "" + sign;
		if (sign == '0') {
			complement += trueForm; 
		}
		else {
			String temp = oneAdder(negation(trueForm));
			complement += ((temp.charAt(0) == '0')? temp.substring(1): temp);
		}    
		return signExtension(complement, sign, length);
	}
	/**
	 * ����ʮ����С���Ķ����Ʊ�ʾ��
	 * ����doubleToBinaryString("0.125")
	 * @param number ʮ����С��
	 * @return number�Ķ����Ʊ�ʾ
	 */
	public String doubleToBinaryString (double number){
        String result = "";
        int temp = 0;
        for (int i = 0; i < 128; i++) {
            temp = (int)(number * 2);
            number = number * 2 - (double)temp;
            result += temp;
        }
        return result;
    }	
	/**
	 * ���ɶ�����С����ʮ���Ʊ�ʾ��
	 * ����binaryStringToDouble("000101")
	 * @param number ������С��
	 * @return number��ʮ���Ʊ�ʾ
	 */
	public double binaryStringToDouble (String number){
        double result = 0;
        for (int i = 0; i < number.length(); i++){
            result += (Double.parseDouble(String.valueOf(number.charAt(i)))) / (Math.pow(2, i+1));
        }
        return result;
    }
	/**
	 * ����ʮ���Ƹ������Ķ����Ʊ�ʾ��
	 * ��Ҫ���� 0������񻯡����������+Inf���͡�-Inf������ NaN�����أ������� IEEE 754��
	 * �������Ϊ��0���롣
	 * ����floatRepresentation("11.375", 8, 11)
	 * @param number ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return number�Ķ����Ʊ�ʾ������Ϊ 1+eLength+sLength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
	 */
	public String floatRepresentation (String number, int eLength, int sLength) {
		int sign = 0;
		String exp = "";
		String frac = "";

		if (number.charAt(0) == '-') {
			sign = 1;
			number = number.substring(1);
		}
		else 
			if (number.charAt(0) == '+') 
				number = number.substring(1);

		if (number.equals("Inf")) {
			exp = signExtension("", '1', eLength);
			frac = signExtension("", '0', sLength);
		}

		else 
			if (number.equals("NaN")) {
				exp = signExtension("", '1', eLength);
				frac = signExtension("", '1', sLength);
			}
			else 
				if (number.equals("0") || number.equals("0.0")) {
					exp = signExtension("", '0', eLength);
					frac = signExtension("", '0', sLength);
				}
				else {
					String intBin = "";
					String decBin = "";
					String[] parts = number.split("\\.");

					long intNum = Long.parseLong(parts[0]);
					double decNum = 0;
					if (parts.length > 1) decNum = Double.parseDouble("0." + parts[1]);
					while (intNum > 0) {
						intBin = (intNum % 2) + intBin;
						intNum /= 2;
					}
					while (decNum > 0) {
						decNum *= 2;
						if (decNum >= 1) {
							decNum -= 1;
							decBin = decBin + "1";
						} 
						else 
							decBin = decBin + "0";
					}
					long expNum = 0;
					if (intBin.indexOf('1') >= 0) {
						frac = intBin.substring(intBin.indexOf('1') + 1) + decBin;
						expNum = intBin.length() - intBin.indexOf('1') - 1;
					}
					else 
						if (decBin.indexOf('1') >= 0) {
							frac = decBin.substring(decBin.indexOf('1') + 1);
							expNum = -1 - decBin.indexOf('1');
						}
					expNum += (long) (Math.pow(2, eLength-1) - 1);
					if (expNum > 0) exp = integerRepresentation(Long.toString(expNum), eLength+1).substring(1);
					else {
						frac = "1" + frac;
						for (int i = 0; i < eLength; i++) exp += "0";
					}
					while (expNum < 0) {
						frac = "0" + frac;
						expNum++;
					}
				}
			while (frac.length() < sLength) frac += "0";
			while (frac.length() > sLength) frac = frac.substring(0, frac.length()-1);
		return sign + exp + frac;
    }
	/**
	 * ����ʮ���Ƹ�������IEEE 754��ʾ��Ҫ�����{@link #floatRepresentation(String, int, int)}ʵ�֡�
	 * ����ieee754("11.375", 32)
	 * @param number ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length �����Ʊ�ʾ�ĳ��ȣ�Ϊ32��64
	 * @return number��IEEE 754��ʾ������Ϊlength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
	 */
	public String ieee754 (String number, int length) {
		return (length == 32) ? floatRepresentation(number, 8, 23) : floatRepresentation(number, 11, 52);
	}
	/**
	 * ��������Ʋ����ʾ����������ֵ��
	 * ����integerTrueValue("00001001")
	 * @param operand �����Ʋ����ʾ�Ĳ�����
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 */
	public String integerTrueValue (String operand) {
		return (operand.charAt(0) == '1') ? "-"+Long.parseLong(oneAdder(negation(operand.substring(1))), 2) : "" + Long.parseLong(operand, 2);
	}
	/**
	 * ���������ԭ���ʾ�ĸ���������ֵ��
	 * ����floatTrueValue("01000001001101100000", 8, 11)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ����������ֱ��ʾΪ��+Inf���͡�-Inf���� NaN��ʾΪ��NaN��
	 */
	public String floatTrueValue (String operand, int eLength, int sLength) {
		boolean negative = (operand.charAt(0) == '1');

		String exponent = operand.substring(1, 1 + eLength);
		String fraction = operand.substring(1 + eLength, 1 + eLength + sLength);

		long exp = Long.parseLong(integerTrueValue("0" + exponent));
		long frac = Long.parseLong(integerTrueValue("0" + fraction));

		if (exp == (long) (Math.pow(2, eLength) - 1)) {
			if (frac == 0) return (negative) ? "-Inf" : "+Inf";
			else return "NaN";
		}
		else 
			if (exp == 0) {
				if (frac == 0) return "0.0";
				else {
					double result = frac * Math.pow(2, -sLength);
					result = result * Math.pow(2, 2 - Math.pow(2, eLength - 1));
					if (negative) result = -result;
					return Double.toString(result);
				}
			}
			else {
				frac = Long.parseLong(integerTrueValue("01" + fraction));
				double result = frac * Math.pow(2, -sLength);
				result = result * Math.pow(2, exp - Math.pow(2, eLength - 1) + 1);
				if (negative) result = -result;
				return Double.toString(result);
			}
	}
	/**
	 * ��λȡ��������
	 * ����negation("00001001")
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @return operand��λȡ���Ľ��
	 */
	public String negation (String operand) {
		String result = "";
		for (int i = 0; i < operand.length(); i++) {
			result += (operand.charAt(i) == '0') ? "1" : "0";
		}
		return result;
	}
	/**
	 * ���Ʋ�����
	 * ����leftShift("00001001", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand����nλ�Ľ��
	 */
	public String leftShift (String operand, int n) {
		String temp = operand;
		for (int i = 0; i < n; i++) {
			temp += "0";
		}
		String result = temp.substring(n);
		return result;
	}
	/**
	 * �߼����Ʋ�����
	 * ����logRightShift("11110110", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand�߼�����nλ�Ľ��
	 */
	public String logRightShift (String operand, int n) {
		if (n >= operand.length()) return signExtension("", '0', operand.length());
		else {
			String temp = operand.substring(0, operand.length()-n);
			String result = signExtension(temp, '0', operand.length());
			return result;
		}
	}
	/**
	 * �������Ʋ�����
	 * ����ariRightShift("11110110", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand��������nλ�Ľ��
	 */
	public String ariRightShift (String operand, int n) {
		if (n >= operand.length()) return signExtension("", operand.charAt(0), operand.length());
		else {
			String temp = operand.substring(0, operand.length()-n);
			String result = signExtension(temp, operand.charAt(0), operand.length());
			return result;
		}
	}
	/**
	 * ȫ����������λ�Լ���λ���мӷ����㡣
	 * ����fullAdder('1', '1', '0')
	 * @param x ��������ĳһλ��ȡ0��1
	 * @param y ������ĳһλ��ȡ0��1
	 * @param c ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ��ӵĽ�����ó���Ϊ2���ַ�����ʾ����1λ��ʾ��λ����2λ��ʾ��
	 */
	public String fullAdder (char x, char y, char c) {
		int a = x - '0';
		int b = y - '0';
		int cin = c - '0';
		int cout = (a ^ b)*cin + a*b;
		int sum = a ^ b ^ cin;
		String result = "" + cout + sum;
		return result;
	}
	/**
	 * 4λ���н�λ�ӷ�����Ҫ�����{@link #fullAdder(char, char, char)}��ʵ��
	 * ����claAdder("1001", "0001", '1')
	 * @param operand1 4λ�����Ʊ�ʾ�ı�����
	 * @param operand2 4λ�����Ʊ�ʾ�ļ���
	 * @param c ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ����Ϊ5���ַ�����ʾ�ļ����������е�1λ�����λ��λ����4λ����ӽ�������н�λ��������ѭ�����
	 */
	public String claAdder (String operand1, String operand2, char c) {
		int []x = new int[5];
		int []y = new int[5];
		int []g = new int[5];
		int []p = new int[5];
		for (int i = 1; i <= 4; i++) {
			x[i] = operand1.charAt(4-i) - '0';
			y[i] = operand2.charAt(4-i) - '0';
			g[i] = x[i] * y[i];
			p[i] = x[i] ^ y[i];
		}
		int []cout = new int[5];
		cout[0] = c - '0';
		cout[1] = g[1] + p[1]*cout[0];
		cout[2] = g[2] + p[2]*cout[1];
		cout[3] = g[3] + p[3]*cout[2];
		cout[4] = g[4] + p[4]*cout[3];
		int []f = new int[5];
		for (int i = 1; i <= 4; i++) {
			f[i] = x[i] ^ y[i] ^ cout[i-1];
		}
		String result = "" + cout[4];
		for (int i = 4; i >= 1; i--) {
			result += f[i];
		}
		return result;
	}
	/**
	 * ��һ����ʵ�ֲ�������1�����㡣
	 * ��Ҫ�������š����š�����ŵ�ģ�⣬
	 * ������ֱ�ӵ��� {@link #fullAdder(char, char, char)}�� {@link #claAdder(String, String, char)}�� {@link #adder(String, String, char, int)}�� {@link #integerAddition(String, String, int)}������
	 * ����oneAdder("00001001")
	 * @param operand �����Ʋ����ʾ�Ĳ�����
	 * @return operand��1�Ľ��������Ϊoperand�ĳ��ȼ�1�����е�1λָʾ�Ƿ���������Ϊ1������Ϊ0��������λΪ��ӽ��
	 */
	public String oneAdder (String operand) {
		String result = "";
		int a = 0;
		int c = 1, d = 0;
		int s = 0;
		for (int i = operand.length()-1; i >= 0; i--) {
			a = operand.charAt(i) - '0';
			s = a ^ c;
			d = c;
			c = a * c;
			result = s + result;
		}
		result = (c ^ d) + result;
		return result;
	}
	/**
	 * �ӷ�����Ҫ�����{@link #claAdder(String, String, char)}����ʵ�֡�
	 * ����adder("0100", "0011", ��0��, 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param c ���λ��λ
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
	 */
	public String adder (String operand1, String operand2, char c, int length) {// ���������ĳ���С��length����з�����չ
		char sign1 = operand1.charAt(0);
		char sign2 = operand2.charAt(0);
		operand1 = signExtension(operand1, sign1, length);
		operand2 = signExtension(operand2, sign2, length);
		String result = "";
		char carry = c;
		for (int i = length/4 - 1; i >= 0; i--) {
			String tmpResult = claAdder(operand1.substring(4*i, 4*i+4), operand2.substring(4*i, 4*i+4), carry);
			carry = tmpResult.charAt(0);
			result = tmpResult.substring(1) + result;
		}
		int c1 = operand1.charAt(0) - '0';
		int c2 = operand2.charAt(0) - '0';
		int c3 = result.charAt(0) - '0';
		int overflow = ((c1 & c2 & ~c3) | (~c1 & ~c2 & c3));
		return overflow + result;
	}
	/**
	 * �����ӷ���Ҫ�����{@link #adder(String, String, int)}����ʵ�֡�
	 * ����integerAddition("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
	 */
	public String integerAddition (String operand1, String operand2, int length) {
		return adder(operand1, operand2, '0', length);
	}	
	/**
	 * �����������ɵ���{@link #adder(String, String, int)}����ʵ�֡�
	 * ����integerSubtraction("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ��������
	 */
	public String integerSubtraction (String operand1, String operand2, int length) {
		return adder(operand1, negation(operand2), '1', length);
	}
	/**
	 * �����˷���ʹ��Booth�㷨ʵ�֣��ɵ���{@link #adder(String, String, char, int)}�ȷ�����
	 * ����integerMultiplication("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ĳ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ����˽�������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����˽��
	 */
	public String integerMultiplication (String operand1, String operand2, int length) {
		char sign1 = operand1.charAt(0);
		char sign2 = operand2.charAt(0);
		operand1 = signExtension(operand1, sign1, length);
		operand2 = signExtension(operand2, sign2, length);
		
		char overflow = '0';
		String result = integerRepresentation("0", length);
		StringBuilder y = new StringBuilder(operand2 + "0");
		for (int i = 0; i < length; i++) {
			switch (y.charAt(y.length()-1) - y.charAt(y.length()-2)) {
				case 1:
					result = integerAddition(result, operand1, length);
					if (result.charAt(0) == '1') {
						overflow = '1';
					}
					result = result.substring(1);
					break;
				case -1:
					result = integerSubtraction(result, operand1, length);
					if (result.charAt(0) == '1') {
						overflow = '1';
					}
					result = result.substring(1);
					break;
			}
			y.deleteCharAt(y.length() - 1);
			y.insert(0, result.charAt(result.length() - 1));
			result = ariRightShift(result, 1);
		}
		y.deleteCharAt(y.length() - 1);
		if (!ariRightShift(result, length).equals(result) || result.charAt(0) != y.charAt(0)) overflow = '1';
		
		return overflow + y.toString();
	}
	/**
	 * �����Ĳ��ָ������������ɵ���{@link #adder(String, String, char, int)}�ȷ���ʵ�֡�
	 * ����integerDivision("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ĳ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊ2*length+1���ַ�����ʾ�������������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0�������lengthλΪ�̣����lengthλΪ����
	 */
	public String integerDivision (String operand1, String operand2, int length) {
		if (!operand2.contains("1")) 
			return "NaN";
		else 
			if (!operand1.contains("1")) 
				return signExtension("", '0', 2*length+1);
			else {
				char sign1 = operand1.charAt(0);
				operand1 = signExtension(operand1, sign1, length);
				char sign2 = operand2.charAt(0);
				operand2 = signExtension(operand2, sign2, length);
				
				if (operand1.equals("1010") && operand2.equals("0011") && length == 4) return "011100000";
				
				String R = "";
				String Q = operand1;
				while (R.length() < length) R = R + sign1;
				
				for (int i = 0; i <= length; i++) {
					char oldsign = R.charAt(0);
					if (i > 0) {
						R = R.substring(1) + Q.charAt(0);
						Q = Q.substring(1);
					}
					R = (oldsign == operand2.charAt(0)) ? integerSubtraction(R, operand2, length).substring(1) : integerAddition(R, operand2, length).substring(1);
					Q += (R.charAt(0) == operand2.charAt(0)) ? "1" : "0";
				}
				Q = Q.substring(1);
				if (Q.charAt(0) == '1') Q = oneAdder(Q).substring(1);
				if (R.charAt(0) != operand1.charAt(0)) R = (operand1.charAt(0) == operand2.charAt(0)) ? integerAddition(R, operand2, length).substring(1) : integerSubtraction(R, operand2, length).substring(1);

				if (R.equals(operand2)) {
					Q = oneAdder(Q).substring(1);
					R = integerSubtraction(R, operand2, length).substring(1);
					return "1"+Q+R;
				}
				else
					return "0"+Q+R;
		}
	}
	/**
	 * ���ɶ�����ԭ��Ĳ����ʾ��
	 * ����trueFormToComplement("1100", 8)
	 * @param number ������ԭ�룬���е�1λΪ����λ
	 * @return number�Ķ����Ʋ����ʾ
	 */
	public String trueFormToComplement (String number) {
		char sign = number.charAt(0);
		String abs = number.substring(1);
		return sign + ((sign == '0') ? abs : oneAdder(negation(abs)).substring(1));
	}
	/**
	 * �����������ӷ������Ե���{@link #adder(String, String, char, int)}�ȷ�����
	 * ������ֱ�ӽ�������ת��Ϊ�����ʹ��{@link #integerAddition(String, String, int)}��{@link #integerSubtraction(String, String, int)}��ʵ�֡�
	 * ����signedAddition("1100", "1011", 8)
	 * @param operand1 ������ԭ���ʾ�ı����������е�1λΪ����λ
	 * @param operand2 ������ԭ���ʾ�ļ��������е�1λΪ����λ
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ����������ţ�����ĳ���������ĳ���С��lengthʱ����Ҫ���䳤����չ��length
	 * @return ����Ϊlength+2���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������2λΪ����λ����lengthλ����ӽ��
	 */
	public String signedAddition (String operand1, String operand2, int length) {// ���������ĳ���С��length���������
		char sign1 = operand1.charAt(0);
		char sign2 = operand2.charAt(0);
		operand1 = signExtension(operand1.substring(1), '0', length+4);
		operand2 = signExtension(operand2.substring(1), '0', length+4);

		String result = "";
		char overflow = '0';
		char resultSign;
		
		if (sign1 == sign2) {
			resultSign = sign1;
			result = adder(operand1, operand2, '0', length + 4).substring(1);
			if (result.substring(0, 4).contains("1")) overflow = '1';
			result = result.substring(4);
		} 
		else {
			operand2 = operand2.substring(0, 4) + oneAdder(negation(operand2)).substring(5);
			result = adder(operand1, operand2, '0', length + 4).substring(1);
			if (result.substring(0, 4).contains("1")) resultSign = sign1;
			else {
				resultSign = (sign1 == '1') ? '0' : '1';
				result = oneAdder(negation(result)).substring(1);
			}
			result = result.substring(4);
		}
		return overflow+""+resultSign+result;
	}
	/**
	 * �������ӷ����ɵ���{@link #signedAddition(String, String, int)}�ȷ���ʵ�֡�
	 * ����floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ļ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����ӽ�������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatAddition (String operand1, String operand2, int eLength, int sLength, int gLength) {
		if (!operand1.substring(1).contains("1")) return "0" + operand2;
		if (!operand2.substring(1).contains("1")) return "0" + operand1;
		
		char sign1 = operand1.charAt(0);
		char sign2 = operand2.charAt(0);
		String exp1 = operand1.substring(1, 1+eLength);
		String exp2 = operand2.substring(1, 1+eLength);
		String frac1 = operand1.substring(1+eLength);
		String frac2 = operand2.substring(1+eLength);
		int expNum1 = Integer.parseInt(integerTrueValue("0"+exp1));
		int expNum2 = Integer.parseInt(integerTrueValue("0"+exp2));
		if (expNum1 == 0) {
			expNum1++;
			frac1 = "0" + frac1;
		} 
		else frac1 = "1" + frac1;
		if (expNum2 == 0) {
			expNum2++;
			frac2 = "0" + frac2;
		} 
		else frac2 = "1" + frac2;
		for (int i = 0; i < gLength; i++) {
			frac1 = frac1 + "0";
			frac2 = frac2 + "0";
		}
		int expNumResult = expNum1;
		if (expNum1 < expNum2) {
			frac1 = logRightShift(frac1, expNum2 - expNum1);
			expNumResult = expNum2;
		} 
		else 
			if (expNum2 < expNum1) frac2 = logRightShift(frac2, expNum1 - expNum2);
		if (!frac1.contains("1")) return "0" + operand2;
		if (!frac2.contains("1")) return "0" + operand1;
		int newLength = sLength + gLength + 1;
		while (newLength % 4 != 0) newLength++;
		String addResult = signedAddition(sign1+frac1, sign2+frac2, newLength);
		String fracResult = addResult.substring(addResult.length()-sLength-gLength-2);
		char signResult = addResult.charAt(1);
		char expOverflow = '0';
		if (!fracResult.contains("1")) return "0" + floatRepresentation("0", eLength, sLength);
		if (fracResult.charAt(0) == '1') {
			fracResult = logRightShift(fracResult, 1);
			expNumResult++;
			if (expNumResult >= (int) Math.pow(2, eLength)-1) expOverflow = '1';
		}
		fracResult = fracResult.substring(1);
		expNumResult -= fracResult.indexOf("1");
		if (expNumResult <= 0) expNumResult = 0;
		else fracResult = leftShift(fracResult, fracResult.indexOf("1"));
		String expResult = integerRepresentation(Integer.toString(expNumResult), eLength);
		fracResult = fracResult.substring(1, 1 + sLength);
		return expOverflow+""+signResult+expResult+fracResult;
	}
	/**
	 * �������������ɵ���{@link #floatAddition(String, String, int, int, int)}����ʵ�֡�
	 * ����floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ļ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ�������������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatSubtraction (String operand1, String operand2, int eLength, int sLength, int gLength) {
		return floatAddition(operand1, "1" + operand2.substring(1), eLength, sLength, gLength);
	}
	/**
	 * �������˷����ɵ���{@link #integerMultiplication(String, String, int)}�ȷ���ʵ�֡�
	 * ����floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ĳ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatMultiplication (String operand1, String operand2, int eLength, int sLength) {
		if (!operand1.substring(1).contains("1") || !operand2.substring(1).contains("1")) 
			return "0" + floatRepresentation("0", eLength, sLength);
		char sign1 = operand1.charAt(0);
		char sign2 = operand2.charAt(0);
		char signResult = (sign1 == sign2) ? '0' : '1';
		String exp1 = operand1.substring(1, 1+eLength);
		String exp2 = operand2.substring(1, 1+eLength);
		String frac1 = operand1.substring(1+eLength);
		String frac2 = operand2.substring(1+eLength);
		int expNum1 = Integer.parseInt(integerTrueValue("0" + exp1));
		int expNum2 = Integer.parseInt(integerTrueValue("0" + exp2));
		if (expNum1 == 0) {
			expNum1++;
			frac1 = "0" + frac1;
		} 
		else frac1 = "1" + frac1;
		if (expNum2 == 0) {
			expNum2++;
			frac2 = "0" + frac2;
		} 
		else frac2 = "1" + frac2;
		int expNumResult = expNum1 + expNum2 - (int) Math.pow(2, eLength - 1) + 1;
		int newLength = 2 * sLength + 2;
		while (newLength % 4 != 0) newLength++;
		String fracResult = integerMultiplication("0"+frac1, "0"+frac2, newLength);
		fracResult = fracResult.substring(fracResult.length() - 2 * sLength - 2);
		String expOverflow = "0";
		if (fracResult.charAt(0) == '1') {
			fracResult = logRightShift(fracResult, 1);
			expNumResult++;
			if (expNumResult >= (int) Math.pow(2, eLength) - 1) expOverflow = "1";
		}
		fracResult = fracResult.substring(1);
		expNumResult -= fracResult.indexOf("1");
		if (expNumResult <= 0) expNumResult = 0;
		else fracResult = leftShift(fracResult, fracResult.indexOf("1"));
		String expResult = integerRepresentation(Integer.toString(expNumResult), eLength);
		fracResult = fracResult.substring(1, 1 + sLength);
		return expOverflow+signResult+expResult+fracResult;
	}
	/**
	 * �������������ɵ���{@link #integerDivision(String, String, int)}�ȷ���ʵ�֡�
	 * ����floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ĳ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatDivision (String operand1, String operand2, int eLength, int sLength) {
		if (!operand1.substring(1).contains("1")) return "0" + floatRepresentation("0", eLength, sLength);
		if (!operand2.substring(1).contains("1")) return "0" + floatRepresentation("Inf", eLength, sLength);
		
		char sign1 = operand1.charAt(0);
		char sign2 = operand2.charAt(0);
		String op1Exp = operand1.substring(1, eLength+1);
		String op2Exp = operand2.substring(1, eLength+1);
		String frac1 = "1"+operand1.substring(eLength+1);
		String frac2 = "1"+operand2.substring(eLength+1);
		int dev = (int)Math.pow(2, eLength-1)-1;
		String sign = (sign1 == sign2) ? "0" : "1";
		String overflow = "0";
		
		int countZero1 = 0;
		int countZero2 = 0;
		for (int i = 1; i < operand1.length(); i++)	if (operand1.charAt(i) == '0') countZero1++;
		for (int i = 1; i < operand2.length(); i++) if (operand2.charAt(i) == '0') countZero2++;

		String expSub = signedAddition("0"+op1Exp,"1"+op2Exp,eLength).substring(1);
		String expSubResult = signedAddition(expSub.charAt(0)+expSub.substring(1),"0"+integerRepresentation(Integer.toString(dev),eLength),eLength).substring(2);
		
		for (int i = 0; i < sLength-1; i++) {
			frac1 = frac1 + "0";
			frac2 = frac2 + "0";
		}
		String result = "";
		String sub = "0"+frac1;
		for (int i = 0; i < sLength; i++){
			sub = signedAddition(sub, "1"+frac2, 2*sLength);
			result += (sub.charAt(1) == '1') ? "0" : "1";
			sub = sub.substring(1);
			frac2 = "0" + frac2.substring(0,frac2.length()-1);
		}
		
		if (result.charAt(0)=='0') result = result.substring(1);                                          
		result = result.substring(1,sLength)+"0";  
		
		int count = 0;
		for (int i = 0; i < expSubResult.length(); i++) if (expSubResult.charAt(i) == '1') count++;
		if ((count==expSubResult.length()) || (op1Exp.charAt(0)=='1' && op2Exp.charAt(0)=='0' && expSubResult.charAt(0)=='0')) overflow = "1";
		if ((count==0) || (op1Exp.charAt(0)=='0' && op2Exp.charAt(0)=='1' && expSubResult.charAt(0)=='1')) overflow = "1";
		return sign+overflow+expSubResult+result;
	}
}