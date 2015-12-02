/* {{{ GPL
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
}}} */

package uk.co.nickthecoder.pinkwino.metadata;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Creates a short summary of a page, with search phrases highlighted.
 */

public class SummaryMaker
{

    /**
     * The full text that needs to be summarised
     */
    private String _content;

    /**
     * The keywords that need to be highlighted. A collection of String objects
     */
    private Collection<String> _keywords;

    public SummaryMaker(String content, Collection<String> keywords)
    {
        _content = content;
        _keywords = keywords;
    }

    public String getContent()
    {
        return _content;
    }

    public Iterator<String> getKeywords()
    {
        return _keywords.iterator();
    }

    protected String createSummary()
    {
        // System.out.println( "Summary section for : " + getContent() );

        String content = getContent();
        String contentUpper = content.toUpperCase();
        TreeSet<SummarySection> matchedSections = new TreeSet<SummarySection>();

        for (Iterator<String> i = getKeywords(); i.hasNext();) {
            String toMatch = i.next().toUpperCase();

            int matchIndex = contentUpper.indexOf(toMatch);
            if (matchIndex >= 0) {
                matchedSections.add(createSummarySection(content, toMatch, matchIndex));
            }
        }

        int previousEnd = -1;
        StringBuffer result = new StringBuffer();
        for (Iterator<SummarySection> i = matchedSections.iterator(); i.hasNext();) {
            SummarySection section = i.next();

            if (previousEnd >= section.getFrom()) {
                if (previousEnd > section.getTo()) {
                    // Do nothing
                } else {
                    // Add just the extra bit
                    result.append(content.substring(previousEnd, section.getTo()));
                    previousEnd = section.getTo();
                }
            } else {

                if (section.getFrom() != 0) {
                    result.append(" ... ");
                }

                result.append(section);
                previousEnd = section.getTo();

            }

        }
        if (previousEnd < content.length()) {
            result.append(" ... ");
        }

        // System.out.println( "Summary : "+ result );

        return result.toString();
    }

    protected SummarySection createSummarySection(String content, String word, int matchIndex)
    {
        int start = walkBackwards(content, matchIndex);
        int end = walkForwards(content, matchIndex + word.length());

        return new SummarySection(content, start, end);
    }

    protected int walkBackwards(String text, int matchIndex)
    {
        if (matchIndex <= 1) {
            return 0;
        }

        // Configuration information
        int requiredWords = 10;
        int requiredCharacters = 100;

        boolean prevWasSpace = false;

        int doneCharacters = 0;
        int doneWords = 0;

        int i = matchIndex - 1;
        while (i >= 0) {

            char c = text.charAt(i);

            // Whitespace
            if (Character.isWhitespace(c)) {
                if (!prevWasSpace) {
                    doneWords++;
                    if (doneWords > requiredWords) {
                        // System.out.println( "Stopped back after word : " +
                        // doneWords );
                        return i;
                    }

                }
                prevWasSpace = true;

            } else {

                doneCharacters++;
                if (doneCharacters > requiredCharacters) {
                    // System.out.println( "Stopped back after character : " +
                    // doneCharacters );
                    return i;
                }

                prevWasSpace = false;
            }

            // Full stop
            if ((c == '.') && prevWasSpace && (doneWords >= requiredWords)) {
                // System.out.println(
                // "Stopped back after full stop - words = : " + doneWords );
                return i;
            }

            i--;
        }

        // System.out.println( "Exited loop : i =  " + i );
        return 0;
    }

    protected int walkForwards(String text, int matchIndex)
    {
        int end = text.length();
        if (matchIndex >= end - 1) {
            return end;
        }

        // Configuration information
        int requiredWords = 10;
        int requiredCharacters = 100;

        boolean prevWasSpace = false;
        boolean prevWasFullStop = false;

        int doneCharacters = 0;
        int doneWords = 0;

        int i = matchIndex;
        while (i < end) {

            char c = text.charAt(i);

            // Whitespace
            if (Character.isWhitespace(c)) {

                if (prevWasFullStop && (doneWords >= requiredWords)) {
                    // System.out.println(
                    // "Stopped forward after full stop - words = : " +
                    // doneWords );
                    return i - 1;
                }

                if (!prevWasSpace) {
                    doneWords++;
                    if (doneWords > requiredWords) {
                        // System.out.println( "Stopped back after word : " +
                        // doneWords );
                        return i;
                    }

                }
                prevWasSpace = true;

            } else {

                doneCharacters++;
                if (doneCharacters > requiredCharacters) {
                    // System.out.println( "Stopped back after character : " +
                    // doneCharacters );
                    return i;
                }

                prevWasSpace = false;
            }

            // Full stop
            prevWasFullStop = (c == '.');

            i++;
        }

        // System.out.println( "Exited loop : i =  " + i );
        return end;
    }

    public Iterator<SummarySection> getSummarySections()
    {
        String content = getContent();
        String upperContent = content.toUpperCase();
        TreeSet<SummarySection> matchedSections = new TreeSet<SummarySection>();

        for (Iterator<String> i = getKeywords(); i.hasNext();) {
            String toMatch = i.next().toUpperCase();

            int nextIndex = 0;
            while ((nextIndex = upperContent.indexOf(toMatch, nextIndex)) >= 0) {
                matchedSections.add(new SummarySection(content, nextIndex, nextIndex + toMatch.length(), true));
                nextIndex = nextIndex + toMatch.length();
            }
        }

        LinkedList<SummarySection> allSections = new LinkedList<SummarySection>();
        int doneIndex = 0;
        for (Iterator<SummarySection> i = matchedSections.iterator(); i.hasNext();) {
            SummarySection matchedSection = i.next();

            if (matchedSection.getFrom() > doneIndex) {
                // Add an unmatched section.
                allSections.add(new SummarySection(content, doneIndex, matchedSection.getFrom(), false));
                doneIndex = matchedSection.getFrom();
            }

            if (matchedSection.getTo() <= doneIndex) {
                // Throw it away (do nothing)
            } else {
                // Add the matched section
                allSections.add(new SummarySection(content, doneIndex, matchedSection.getTo(), true));
                doneIndex = matchedSection.getTo();
            }

        }

        // Add the remaining unmatched section at the end.
        if (doneIndex < content.length()) {
            allSections.add(new SummarySection(content, doneIndex, content.length(), false));
        }

        return allSections.iterator();
    }

    // -------------------- [[Inner Class SummarySection]] --------------------

    public class SummarySection implements Comparable<SummarySection>
    {

        private String _text;
        private int _from;
        private int _to;
        private boolean _isMatched;

        public SummarySection(String text, int from, int to)
        {
            this(text, from, to, true);
        }

        public SummarySection(String text, int from, int to, boolean isMatched)
        {
            _text = text;
            _from = from;
            _to = to;
            _isMatched = isMatched;
        }

        public boolean isMatched()
        {
            return _isMatched;
        }

        public String toString()
        {
            return _text.substring(_from, _to);
        }

        public int getFrom()
        {
            return _from;
        }

        public int getTo()
        {
            return _to;
        }

        public int compareTo(SummarySection o)
        {
            return _from - o.getFrom();
        }

    }

}
