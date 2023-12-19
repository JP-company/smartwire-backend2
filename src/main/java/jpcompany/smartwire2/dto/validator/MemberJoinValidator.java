package jpcompany.smartwire2.dto.validator;

import java.util.HashSet;
import java.util.Set;

public class MemberJoinValidator {
    final int PASSWORD_MINIMUM_LENGTH = 10;
    final int PASSWORD_MAXIMUM_LENGTH = 20;
    final int COMPANY_NAME_MAXIMUM_LENGTH = 20;
    final String BLANK = " ";
    
    public void validate(String loginEmail, String loginPassword, String loginPasswordVerify, String companyName) {
        validateLoginEmail(loginEmail);
        validateLoginPassword(loginPassword, loginPasswordVerify);
        validateCompanyName(companyName);
    }

    private void validateLoginEmail(String loginEmail) {
        validateEmptyEmail(loginEmail);
        validateEmailForm(loginEmail);
    }

    private void validateEmailForm(String loginEmail) {
        final String AT = "@";
        final String DOT = ".";
        if (loginEmail.startsWith(AT) || loginEmail.startsWith(DOT) ||
                loginEmail.endsWith(AT) || loginEmail.endsWith(DOT)) {
            throw new IllegalArgumentException("[EMAIL_ERROR] 올바른 이메일 형식이 아닙니다.");
        }
        if (!loginEmail.contains(AT) || loginEmail.indexOf(AT) != loginEmail.lastIndexOf(AT)) {
            throw new IllegalArgumentException("[EMAIL_ERROR] 올바른 이메일 형식이 아닙니다.");
        }
        if (!loginEmail.split(AT)[1].contains(DOT)) {
            throw new IllegalArgumentException("[EMAIL_ERROR] 올바른 이메일 형식이 아닙니다.");
        }
    }

    private void validateEmptyEmail(String loginEmail) {
        if (loginEmail.isEmpty()) {
            throw new IllegalArgumentException("[EMAIL_ERROR] 이메일을 입력해 주세요.");
        }
    }

    private void validateLoginPassword(String loginPassword, String loginPasswordVerify) {
        validateEmptyPassword(loginPassword);
        validateContainsSpace(loginPassword);
        validateEachPasswordByPasswordPolicy(loginPassword);
        validatePasswordMatches(loginPassword, loginPasswordVerify);
    }

    private void validateEmptyPassword(String loginPassword) {
        if (loginPassword.length() < PASSWORD_MINIMUM_LENGTH || loginPassword.length() > PASSWORD_MAXIMUM_LENGTH) {
            throw new IllegalArgumentException("[PASSWORD_ERROR] 비밀번호는 10자 이상 20자 이하 이어야 합니다.");
        }
    }

    private void validateContainsSpace(String loginPassword) {
        if (loginPassword.contains(BLANK)) {
            throw new IllegalArgumentException("[PASSWORD_ERROR] 비밀번호에 공백을 입력할 수 없습니다.");
        }
    }

    private void validateEachPasswordByPasswordPolicy(String loginPassword) {
        Set<String> typeOfCharactersIncluded = classifyEachPassword(loginPassword);
        if (typeOfCharactersIncluded.size() < Ascii.TOTAL_NUMBERS_OF_CHARACTER_TYPE) {
            throw new IllegalArgumentException("[PASSWORD_ERROR] 비밀번호는 영대소문자, 숫자, 지정된 특수문자를 각각 1개씩 필수적으로 포함헤야 합니다.");
        }
    }

    private Set<String> classifyEachPassword(String loginPassword) {
        Set<String> typeOfCharactersIncluded = new HashSet<>();
        for (int i = 0; i < loginPassword.length(); i++) {
            int passwordCharacter = loginPassword.charAt(i);
            validateValidRange(passwordCharacter);

            String CharacterType = Ascii.classifyWhichCharacter(passwordCharacter);
            typeOfCharactersIncluded.add(CharacterType);
        }
        return typeOfCharactersIncluded;
    }

    private void validateValidRange(int passwordCharacter) {
        if (Ascii.isNotInRange(passwordCharacter)) {
            throw new IllegalArgumentException("[PASSWORD_ERROR] 비밀번호는 영대소문자, 숫자, 지정된 특수문자만 입력 가능 합니다.");
        }
    }

    private void validatePasswordMatches(String loginPassword, String loginPasswordVerify) {
        if (!loginPassword.equals(loginPasswordVerify)) {
            throw new IllegalArgumentException("[PASSWORD_ERROR] 비밀번호가 일치하지 않습니다.");
        }
    }

    private void validateCompanyName(String companyName) {
        validateEmptyCompanyName(companyName);
        validateMaximumLength(companyName);
    }

    private void validateEmptyCompanyName(String companyName) {
        if (companyName.isEmpty()) {
            throw new IllegalArgumentException("[COMPANY_NAME_ERROR] 회사 이름을 입력해 주세요.");
        }
    }

    private void validateMaximumLength(String companyName) {
        if (companyName.length() > COMPANY_NAME_MAXIMUM_LENGTH) {
            throw new IllegalArgumentException("[COMPANY_NAME_ERROR] 회사 이름의 최대 길이는 20자 입니다.");
        }
    }

    private enum Ascii {
        MINIMUM_RANGE(33),
        MAXIMUM_RANGE(126),
        LOWERCASE_MINIMUM_RANGE(97),
        LOWERCASE_MAXIMUM_RANGE(122),
        UPPERCASE_MINIMUM_RANGE(65),
        UPPERCASE_MAXIMUM_RANGE(90),
        NUMBER_MINIMUM_RANGE(48),
        NUMBER_MAXIMUM_RANGE(57);

        public static final int TOTAL_NUMBERS_OF_CHARACTER_TYPE = 4;
        private static final String LOWERCASE = "lowercase";
        private static final String UPPERCASE = "uppercase";
        private static final String NUMBER = "number";
        private static final String SPECIAL_CHARACTER = "special character";

        private final int index;

        Ascii(int index) {
            this.index = index;
        }

        public static String classifyWhichCharacter(int passwordCharacter) {
            if (isLowercase(passwordCharacter)) {
                return LOWERCASE;
            }
            if (isUppercase(passwordCharacter)) {
                return UPPERCASE;
            }
            if (isNumber(passwordCharacter)) {
                return NUMBER;
            }
            return SPECIAL_CHARACTER;
        }

        public static boolean isNotInRange(int asciiCode) {
            return asciiCode < MINIMUM_RANGE.index  && asciiCode < MAXIMUM_RANGE.index;
        }

        private static boolean isLowercase(int asciiCode) {
            return asciiCode >= LOWERCASE_MINIMUM_RANGE.index
                    && asciiCode <= LOWERCASE_MAXIMUM_RANGE.index;
        }

        private static boolean isUppercase(int asciiCode) {
            return asciiCode >= UPPERCASE_MINIMUM_RANGE.index
                    && asciiCode <= UPPERCASE_MAXIMUM_RANGE.index;
        }

        private static boolean isNumber(int asciiCode) {
            return asciiCode >= NUMBER_MINIMUM_RANGE.index
                    && asciiCode <= NUMBER_MAXIMUM_RANGE.index;
        }
    }
}
