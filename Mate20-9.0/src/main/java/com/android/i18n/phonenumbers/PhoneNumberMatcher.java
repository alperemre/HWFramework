package com.android.i18n.phonenumbers;

import com.android.i18n.phonenumbers.PhoneNumberUtil;
import com.android.i18n.phonenumbers.Phonemetadata;
import com.android.i18n.phonenumbers.Phonenumber;
import gov.nist.core.Separators;
import java.lang.Character;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class PhoneNumberMatcher implements Iterator<PhoneNumberMatch> {
    private static final Pattern[] INNER_MATCHES = {Pattern.compile("/+(.*)"), Pattern.compile("(\\([^(]*)"), Pattern.compile("(?:\\p{Z}-|-\\p{Z})\\p{Z}*(.+)"), Pattern.compile("[‒-―－]\\p{Z}*(.+)"), Pattern.compile("\\.+\\p{Z}*([^.]+)"), Pattern.compile("\\p{Z}+(\\P{Z}+)")};
    private static final Pattern LEAD_CLASS;
    private static final Pattern MATCHING_BRACKETS;
    private static final Pattern PATTERN;
    private static final Pattern PUB_PAGES = Pattern.compile("\\d{1,5}-+\\d{1,5}\\s{0,4}\\(\\d{1,4}");
    private static final Pattern SLASH_SEPARATED_DATES = Pattern.compile("(?:(?:[0-3]?\\d/[01]?\\d)|(?:[01]?\\d/[0-3]?\\d))/(?:[12]\\d)?\\d{2}");
    private static final Pattern TIME_STAMPS = Pattern.compile("[12]\\d{3}[-/]?[01]\\d[-/]?[0-3]\\d +[0-2]\\d$");
    private static final Pattern TIME_STAMPS_SUFFIX = Pattern.compile(":[0-5]\\d");
    private PhoneNumberMatch lastMatch = null;
    private final PhoneNumberUtil.Leniency leniency;
    private long maxTries;
    private final PhoneNumberUtil phoneUtil;
    private final String preferredRegion;
    private int searchIndex = 0;
    private State state = State.NOT_READY;
    private final CharSequence text;

    interface NumberGroupingChecker {
        boolean checkGroups(PhoneNumberUtil phoneNumberUtil, Phonenumber.PhoneNumber phoneNumber, StringBuilder sb, String[] strArr);
    }

    private enum State {
        NOT_READY,
        READY,
        DONE
    }

    static {
        String nonParens = "[^" + "(\\[（［" + ")\\]）］" + "]";
        String bracketPairLimit = limit(0, 3);
        MATCHING_BRACKETS = Pattern.compile("(?:[" + "(\\[（［" + "])?(?:" + nonParens + "+[" + ")\\]）］" + "])?" + nonParens + "+(?:[" + "(\\[（［" + "]" + nonParens + "+[" + ")\\]）］" + "])" + bracketPairLimit + nonParens + Separators.STAR);
        String leadLimit = limit(0, 2);
        String punctuationLimit = limit(0, 4);
        String blockLimit = limit(0, 20);
        String punctuation = "[-x‐-―−ー－-／  ­​⁠　()（）［］.\\[\\]/~⁓∼～]" + punctuationLimit;
        String digitSequence = "\\p{Nd}" + limit(1, 20);
        String leadClassChars = "(\\[（［" + "+＋";
        LEAD_CLASS = Pattern.compile("[" + leadClassChars + "]");
        PATTERN = Pattern.compile("(?:" + leadClass + punctuation + Separators.RPAREN + leadLimit + digitSequence + "(?:" + punctuation + digitSequence + Separators.RPAREN + blockLimit + "(?:" + PhoneNumberUtil.EXTN_PATTERNS_FOR_MATCHING + ")?", 66);
    }

    private static String limit(int lower, int upper) {
        if (lower < 0 || upper <= 0 || upper < lower) {
            throw new IllegalArgumentException();
        }
        return "{" + lower + Separators.COMMA + upper + "}";
    }

    PhoneNumberMatcher(PhoneNumberUtil util, CharSequence text2, String country, PhoneNumberUtil.Leniency leniency2, long maxTries2) {
        if (util == null || leniency2 == null) {
            throw new NullPointerException();
        } else if (maxTries2 >= 0) {
            this.phoneUtil = util;
            this.text = text2 != null ? text2 : "";
            this.preferredRegion = country;
            this.leniency = leniency2;
            this.maxTries = maxTries2;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private PhoneNumberMatch find(int index) {
        Matcher matcher = PATTERN.matcher(this.text);
        while (this.maxTries > 0 && matcher.find(index)) {
            int start = matcher.start();
            CharSequence candidate = trimAfterFirstMatch(PhoneNumberUtil.SECOND_NUMBER_START_PATTERN, this.text.subSequence(start, matcher.end()));
            PhoneNumberMatch match = extractMatch(candidate, start);
            if (match != null) {
                return match;
            }
            index = start + candidate.length();
            this.maxTries--;
        }
        return null;
    }

    private static CharSequence trimAfterFirstMatch(Pattern pattern, CharSequence candidate) {
        Matcher trailingCharsMatcher = pattern.matcher(candidate);
        if (trailingCharsMatcher.find()) {
            return candidate.subSequence(0, trailingCharsMatcher.start());
        }
        return candidate;
    }

    static boolean isLatinLetter(char letter) {
        boolean z = false;
        if (!Character.isLetter(letter) && Character.getType(letter) != 6) {
            return false;
        }
        Character.UnicodeBlock block = Character.UnicodeBlock.of(letter);
        if (block.equals(Character.UnicodeBlock.BASIC_LATIN) || block.equals(Character.UnicodeBlock.LATIN_1_SUPPLEMENT) || block.equals(Character.UnicodeBlock.LATIN_EXTENDED_A) || block.equals(Character.UnicodeBlock.LATIN_EXTENDED_ADDITIONAL) || block.equals(Character.UnicodeBlock.LATIN_EXTENDED_B) || block.equals(Character.UnicodeBlock.COMBINING_DIACRITICAL_MARKS)) {
            z = true;
        }
        return z;
    }

    private static boolean isInvalidPunctuationSymbol(char character) {
        return character == '%' || Character.getType(character) == 26;
    }

    private PhoneNumberMatch extractMatch(CharSequence candidate, int offset) {
        if (SLASH_SEPARATED_DATES.matcher(candidate).find()) {
            return null;
        }
        if (TIME_STAMPS.matcher(candidate).find()) {
            if (TIME_STAMPS_SUFFIX.matcher(this.text.toString().substring(candidate.length() + offset)).lookingAt()) {
                return null;
            }
        }
        PhoneNumberMatch match = parseAndVerify(candidate, offset);
        if (match != null) {
            return match;
        }
        return extractInnerMatch(candidate, offset);
    }

    private PhoneNumberMatch extractInnerMatch(CharSequence candidate, int offset) {
        int i;
        CharSequence charSequence = candidate;
        int i2 = offset;
        Pattern[] patternArr = INNER_MATCHES;
        int length = patternArr.length;
        int i3 = 0;
        int i4 = 0;
        while (i4 < length) {
            Matcher groupMatcher = patternArr[i4].matcher(charSequence);
            boolean isFirstMatch = true;
            while (groupMatcher.find() && this.maxTries > 0) {
                if (isFirstMatch) {
                    PhoneNumberMatch match = parseAndVerify(trimAfterFirstMatch(PhoneNumberUtil.UNWANTED_END_CHAR_PATTERN, charSequence.subSequence(i3, groupMatcher.start())), i2);
                    if (match != null) {
                        return match;
                    }
                    i = i4;
                    this.maxTries--;
                    isFirstMatch = false;
                } else {
                    i = i4;
                }
                PhoneNumberMatch match2 = parseAndVerify(trimAfterFirstMatch(PhoneNumberUtil.UNWANTED_END_CHAR_PATTERN, groupMatcher.group(1)), groupMatcher.start(1) + i2);
                if (match2 != null) {
                    return match2;
                }
                this.maxTries--;
                i4 = i;
                i3 = 0;
            }
            i4++;
            i3 = 0;
        }
        return null;
    }

    private PhoneNumberMatch parseAndVerify(CharSequence candidate, int offset) {
        try {
            if (MATCHING_BRACKETS.matcher(candidate).matches()) {
                if (!PUB_PAGES.matcher(candidate).find()) {
                    if (this.leniency.compareTo(PhoneNumberUtil.Leniency.VALID) >= 0) {
                        if (offset > 0 && !LEAD_CLASS.matcher(candidate).lookingAt()) {
                            char previousChar = this.text.charAt(offset - 1);
                            if (isInvalidPunctuationSymbol(previousChar) || isLatinLetter(previousChar)) {
                                return null;
                            }
                        }
                        int lastCharIndex = candidate.length() + offset;
                        if (lastCharIndex < this.text.length()) {
                            char nextChar = this.text.charAt(lastCharIndex);
                            if (isInvalidPunctuationSymbol(nextChar) || isLatinLetter(nextChar)) {
                                return null;
                            }
                        }
                    }
                    Phonenumber.PhoneNumber number = this.phoneUtil.parseAndKeepRawInput(candidate, this.preferredRegion);
                    if ((!this.phoneUtil.getRegionCodeForCountryCode(number.getCountryCode()).equals("IL") || this.phoneUtil.getNationalSignificantNumber(number).length() != 4 || (offset != 0 && (offset <= 0 || this.text.charAt(offset - 1) == '*'))) && this.leniency.verify(number, candidate, this.phoneUtil)) {
                        number.clearCountryCodeSource();
                        number.clearRawInput();
                        number.clearPreferredDomesticCarrierCode();
                        return new PhoneNumberMatch(offset, candidate.toString(), number);
                    }
                    return null;
                }
            }
            return null;
        } catch (NumberParseException e) {
        }
    }

    static boolean allNumberGroupsRemainGrouped(PhoneNumberUtil util, Phonenumber.PhoneNumber number, StringBuilder normalizedCandidate, String[] formattedNumberGroups) {
        int fromIndex = 0;
        if (number.getCountryCodeSource() != Phonenumber.PhoneNumber.CountryCodeSource.FROM_DEFAULT_COUNTRY) {
            String countryCode = Integer.toString(number.getCountryCode());
            fromIndex = normalizedCandidate.indexOf(countryCode) + countryCode.length();
        }
        int fromIndex2 = fromIndex;
        int i = 0;
        while (i < formattedNumberGroups.length) {
            int fromIndex3 = normalizedCandidate.indexOf(formattedNumberGroups[i], fromIndex2);
            if (fromIndex3 < 0) {
                return false;
            }
            fromIndex2 = fromIndex3 + formattedNumberGroups[i].length();
            if (i != 0 || fromIndex2 >= normalizedCandidate.length() || util.getNddPrefixForRegion(util.getRegionCodeForCountryCode(number.getCountryCode()), true) == null || !Character.isDigit(normalizedCandidate.charAt(fromIndex2))) {
                i++;
            } else {
                return normalizedCandidate.substring(fromIndex2 - formattedNumberGroups[i].length()).startsWith(util.getNationalSignificantNumber(number));
            }
        }
        return normalizedCandidate.substring(fromIndex2).contains(number.getExtension());
    }

    static boolean allNumberGroupsAreExactlyPresent(PhoneNumberUtil util, Phonenumber.PhoneNumber number, StringBuilder normalizedCandidate, String[] formattedNumberGroups) {
        String[] candidateGroups = PhoneNumberUtil.NON_DIGITS_PATTERN.split(normalizedCandidate.toString());
        boolean z = true;
        int candidateNumberGroupIndex = number.hasExtension() ? candidateGroups.length - 2 : candidateGroups.length - 1;
        if (candidateGroups.length == 1 || candidateGroups[candidateNumberGroupIndex].contains(util.getNationalSignificantNumber(number))) {
            return true;
        }
        int formattedNumberGroupIndex = formattedNumberGroups.length - 1;
        while (formattedNumberGroupIndex > 0 && candidateNumberGroupIndex >= 0) {
            if (!candidateGroups[candidateNumberGroupIndex].equals(formattedNumberGroups[formattedNumberGroupIndex])) {
                return false;
            }
            formattedNumberGroupIndex--;
            candidateNumberGroupIndex--;
        }
        if (candidateNumberGroupIndex < 0 || !candidateGroups[candidateNumberGroupIndex].endsWith(formattedNumberGroups[0])) {
            z = false;
        }
        return z;
    }

    private static String[] getNationalNumberGroups(PhoneNumberUtil util, Phonenumber.PhoneNumber number, Phonemetadata.NumberFormat formattingPattern) {
        if (formattingPattern != null) {
            return util.formatNsnUsingPattern(util.getNationalSignificantNumber(number), formattingPattern, PhoneNumberUtil.PhoneNumberFormat.RFC3966).split("-");
        }
        String rfc3966Format = util.format(number, PhoneNumberUtil.PhoneNumberFormat.RFC3966);
        int endIndex = rfc3966Format.indexOf(59);
        if (endIndex < 0) {
            endIndex = rfc3966Format.length();
        }
        return rfc3966Format.substring(rfc3966Format.indexOf(45) + 1, endIndex).split("-");
    }

    static boolean checkNumberGroupingIsValid(Phonenumber.PhoneNumber number, CharSequence candidate, PhoneNumberUtil util, NumberGroupingChecker checker) {
        StringBuilder normalizedCandidate = PhoneNumberUtil.normalizeDigits(candidate, true);
        if (checker.checkGroups(util, number, normalizedCandidate, getNationalNumberGroups(util, number, null))) {
            return true;
        }
        Phonemetadata.PhoneMetadata alternateFormats = MetadataManager.getAlternateFormatsForCountry(number.getCountryCode());
        if (alternateFormats != null) {
            for (Phonemetadata.NumberFormat alternateFormat : alternateFormats.numberFormats()) {
                if (checker.checkGroups(util, number, normalizedCandidate, getNationalNumberGroups(util, number, alternateFormat))) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean containsMoreThanOneSlashInNationalNumber(Phonenumber.PhoneNumber number, String candidate) {
        int firstSlashInBodyIndex = candidate.indexOf(47);
        if (firstSlashInBodyIndex < 0) {
            return false;
        }
        int secondSlashInBodyIndex = candidate.indexOf(47, firstSlashInBodyIndex + 1);
        if (secondSlashInBodyIndex < 0) {
            return false;
        }
        if (!(number.getCountryCodeSource() == Phonenumber.PhoneNumber.CountryCodeSource.FROM_NUMBER_WITH_PLUS_SIGN || number.getCountryCodeSource() == Phonenumber.PhoneNumber.CountryCodeSource.FROM_NUMBER_WITHOUT_PLUS_SIGN) || !PhoneNumberUtil.normalizeDigitsOnly(candidate.substring(0, firstSlashInBodyIndex)).equals(Integer.toString(number.getCountryCode()))) {
            return true;
        }
        return candidate.substring(secondSlashInBodyIndex + 1).contains(Separators.SLASH);
    }

    static boolean containsOnlyValidXChars(Phonenumber.PhoneNumber number, String candidate, PhoneNumberUtil util) {
        int index = 0;
        while (index < candidate.length() - 1) {
            char charAtIndex = candidate.charAt(index);
            if (charAtIndex == 'x' || charAtIndex == 'X') {
                char charAtNextIndex = candidate.charAt(index + 1);
                if (charAtNextIndex == 'x' || charAtNextIndex == 'X') {
                    index++;
                    if (util.isNumberMatch(number, (CharSequence) candidate.substring(index)) != PhoneNumberUtil.MatchType.NSN_MATCH) {
                        return false;
                    }
                } else if (!PhoneNumberUtil.normalizeDigitsOnly(candidate.substring(index)).equals(number.getExtension())) {
                    return false;
                }
            }
            index++;
        }
        return true;
    }

    static boolean isNationalPrefixPresentIfRequired(Phonenumber.PhoneNumber number, PhoneNumberUtil util) {
        if (number.getCountryCodeSource() != Phonenumber.PhoneNumber.CountryCodeSource.FROM_DEFAULT_COUNTRY) {
            return true;
        }
        Phonemetadata.PhoneMetadata metadata = util.getMetadataForRegion(util.getRegionCodeForCountryCode(number.getCountryCode()));
        if (metadata == null) {
            return true;
        }
        Phonemetadata.NumberFormat formatRule = util.chooseFormattingPatternForNumber(metadata.numberFormats(), util.getNationalSignificantNumber(number));
        if (formatRule == null || formatRule.getNationalPrefixFormattingRule().length() <= 0 || formatRule.getNationalPrefixOptionalWhenFormatting() || PhoneNumberUtil.formattingRuleHasFirstGroupOnly(formatRule.getNationalPrefixFormattingRule())) {
            return true;
        }
        return util.maybeStripNationalPrefixAndCarrierCode(new StringBuilder(PhoneNumberUtil.normalizeDigitsOnly(number.getRawInput())), metadata, null);
    }

    public boolean hasNext() {
        if (this.state == State.NOT_READY) {
            this.lastMatch = find(this.searchIndex);
            if (this.lastMatch == null) {
                this.state = State.DONE;
            } else {
                this.searchIndex = this.lastMatch.end();
                this.state = State.READY;
            }
        }
        return this.state == State.READY;
    }

    public PhoneNumberMatch next() {
        if (hasNext()) {
            PhoneNumberMatch result = this.lastMatch;
            this.lastMatch = null;
            this.state = State.NOT_READY;
            return result;
        }
        throw new NoSuchElementException();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
