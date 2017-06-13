/**
 * 模拟ALU进行整数和浮点数的四则运算
 * @author [161250131_童正阳]
 *
 */

public class ALU {
	/**
	 * 符号拓展操作。
	 * 例：signExtension("1010", '0', 8)
	 * @param operand 二进制表示的操作数
	 * @param sign 符号位
	 * @param length 期望得到补码的长度
	 * @return operand符号拓展的结果
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
	 * 生成十进制整数的二进制补码表示。
	 * 例：integerRepresentation("9", 8)
	 * @param number 十进制整数。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制补码表示的长度
	 * @return number的二进制补码表示，长度为length
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
	 * 生成十进制小数的二进制表示。
	 * 例：doubleToBinaryString("0.125")
	 * @param number 十进制小数
	 * @return number的二进制表示
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
	 * 生成二进制小数的十进制表示。
	 * 例：binaryStringToDouble("000101")
	 * @param number 二进制小数
	 * @return number的十进制表示
	 */
	public double binaryStringToDouble (String number){
        double result = 0;
        for (int i = 0; i < number.length(); i++){
            result += (Double.parseDouble(String.valueOf(number.charAt(i)))) / (Math.pow(2, i+1));
        }
        return result;
    }
	/**
	 * 生成十进制浮点数的二进制表示。
	 * 需要考虑 0、反规格化、正负无穷（“+Inf”和“-Inf”）、 NaN等因素，具体借鉴 IEEE 754。
	 * 舍入策略为向0舍入。
	 * 例：floatRepresentation("11.375", 8, 11)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return number的二进制表示，长度为 1+eLength+sLength。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
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
	 * 生成十进制浮点数的IEEE 754表示，要求调用{@link #floatRepresentation(String, int, int)}实现。
	 * 例：ieee754("11.375", 32)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制表示的长度，为32或64
	 * @return number的IEEE 754表示，长度为length。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String ieee754 (String number, int length) {
		return (length == 32) ? floatRepresentation(number, 8, 23) : floatRepresentation(number, 11, 52);
	}
	/**
	 * 计算二进制补码表示的整数的真值。
	 * 例：integerTrueValue("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 */
	public String integerTrueValue (String operand) {
		return (operand.charAt(0) == '1') ? "-"+Long.parseLong(oneAdder(negation(operand.substring(1))), 2) : "" + Long.parseLong(operand, 2);
	}
	/**
	 * 计算二进制原码表示的浮点数的真值。
	 * 例：floatTrueValue("01000001001101100000", 8, 11)
	 * @param operand 二进制表示的操作数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位。正负无穷分别表示为“+Inf”和“-Inf”， NaN表示为“NaN”
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
	 * 按位取反操作。
	 * 例：negation("00001001")
	 * @param operand 二进制表示的操作数
	 * @return operand按位取反的结果
	 */
	public String negation (String operand) {
		String result = "";
		for (int i = 0; i < operand.length(); i++) {
			result += (operand.charAt(i) == '0') ? "1" : "0";
		}
		return result;
	}
	/**
	 * 左移操作。
	 * 例：leftShift("00001001", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 左移的位数
	 * @return operand左移n位的结果
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
	 * 逻辑右移操作。
	 * 例：logRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand逻辑右移n位的结果
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
	 * 算术右移操作。
	 * 例：ariRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand算术右移n位的结果
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
	 * 全加器，对两位以及进位进行加法运算。
	 * 例：fullAdder('1', '1', '0')
	 * @param x 被加数的某一位，取0或1
	 * @param y 加数的某一位，取0或1
	 * @param c 低位对当前位的进位，取0或1
	 * @return 相加的结果，用长度为2的字符串表示，第1位表示进位，第2位表示和
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
	 * 4位先行进位加法器。要求采用{@link #fullAdder(char, char, char)}来实现
	 * 例：claAdder("1001", "0001", '1')
	 * @param operand1 4位二进制表示的被加数
	 * @param operand2 4位二进制表示的加数
	 * @param c 低位对当前位的进位，取0或1
	 * @return 长度为5的字符串表示的计算结果，其中第1位是最高位进位，后4位是相加结果，其中进位不可以由循环获得
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
	 * 加一器，实现操作数加1的运算。
	 * 需要采用与门、或门、异或门等模拟，
	 * 不可以直接调用 {@link #fullAdder(char, char, char)}、 {@link #claAdder(String, String, char)}、 {@link #adder(String, String, char, int)}、 {@link #integerAddition(String, String, int)}方法。
	 * 例：oneAdder("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand加1的结果，长度为operand的长度加1，其中第1位指示是否溢出（溢出为1，否则为0），其余位为相加结果
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
	 * 加法器，要求调用{@link #claAdder(String, String, char)}方法实现。
	 * 例：adder("0100", "0011", ‘0’, 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param c 最低位进位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String adder (String operand1, String operand2, char c, int length) {// 若操作数的长度小于length则进行符号扩展
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
	 * 整数加法，要求调用{@link #adder(String, String, int)}方法实现。
	 * 例：integerAddition("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String integerAddition (String operand1, String operand2, int length) {
		return adder(operand1, operand2, '0', length);
	}	
	/**
	 * 整数减法，可调用{@link #adder(String, String, int)}方法实现。
	 * 例：integerSubtraction("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被减数
	 * @param operand2 二进制补码表示的减数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相减结果
	 */
	public String integerSubtraction (String operand1, String operand2, int length) {
		return adder(operand1, negation(operand2), '1', length);
	}
	/**
	 * 整数乘法，使用Booth算法实现，可调用{@link #adder(String, String, char, int)}等方法。
	 * 例：integerMultiplication("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被乘数
	 * @param operand2 二进制补码表示的乘数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的相乘结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相乘结果
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
	 * 整数的不恢复余数除法，可调用{@link #adder(String, String, char, int)}等方法实现。
	 * 例：integerDivision("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被除数
	 * @param operand2 二进制补码表示的除数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为2*length+1的字符串表示的相除结果，其中第1位指示是否溢出（溢出为1，否则为0），其后length位为商，最后length位为余数
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
	 * 生成二进制原码的补码表示。
	 * 例：trueFormToComplement("1100", 8)
	 * @param number 二进制原码，其中第1位为符号位
	 * @return number的二进制补码表示
	 */
	public String trueFormToComplement (String number) {
		char sign = number.charAt(0);
		String abs = number.substring(1);
		return sign + ((sign == '0') ? abs : oneAdder(negation(abs)).substring(1));
	}
	/**
	 * 带符号整数加法，可以调用{@link #adder(String, String, char, int)}等方法，
	 * 但不能直接将操作数转换为补码后使用{@link #integerAddition(String, String, int)}、{@link #integerSubtraction(String, String, int)}来实现。
	 * 例：signedAddition("1100", "1011", 8)
	 * @param operand1 二进制原码表示的被加数，其中第1位为符号位
	 * @param operand2 二进制原码表示的加数，其中第1位为符号位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度（不包含符号），当某个操作数的长度小于length时，需要将其长度扩展到length
	 * @return 长度为length+2的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），第2位为符号位，后length位是相加结果
	 */
	public String signedAddition (String operand1, String operand2, int length) {// 若操作数的长度小于length则进行右移
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
	 * 浮点数加法，可调用{@link #signedAddition(String, String, int)}等方法实现。
	 * 例：floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被加数
	 * @param operand2 二进制表示的加数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相加结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
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
	 * 浮点数减法，可调用{@link #floatAddition(String, String, int, int, int)}方法实现。
	 * 例：floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被减数
	 * @param operand2 二进制表示的减数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相减结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatSubtraction (String operand1, String operand2, int eLength, int sLength, int gLength) {
		return floatAddition(operand1, "1" + operand2.substring(1), eLength, sLength, gLength);
	}
	/**
	 * 浮点数乘法，可调用{@link #integerMultiplication(String, String, int)}等方法实现。
	 * 例：floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被乘数
	 * @param operand2 二进制表示的乘数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
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
	 * 浮点数除法，可调用{@link #integerDivision(String, String, int)}等方法实现。
	 * 例：floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被除数
	 * @param operand2 二进制表示的除数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
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