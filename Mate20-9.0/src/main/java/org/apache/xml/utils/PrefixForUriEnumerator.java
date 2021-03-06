package org.apache.xml.utils;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/* compiled from: NamespaceSupport2 */
class PrefixForUriEnumerator implements Enumeration {
    private Enumeration allPrefixes;
    private String lookahead = null;
    private NamespaceSupport2 nsup;
    private String uri;

    PrefixForUriEnumerator(NamespaceSupport2 nsup2, String uri2, Enumeration allPrefixes2) {
        this.nsup = nsup2;
        this.uri = uri2;
        this.allPrefixes = allPrefixes2;
    }

    public boolean hasMoreElements() {
        if (this.lookahead != null) {
            return true;
        }
        while (this.allPrefixes.hasMoreElements()) {
            String prefix = (String) this.allPrefixes.nextElement();
            if (this.uri.equals(this.nsup.getURI(prefix))) {
                this.lookahead = prefix;
                return true;
            }
        }
        return false;
    }

    public Object nextElement() {
        if (hasMoreElements()) {
            String tmp = this.lookahead;
            this.lookahead = null;
            return tmp;
        }
        throw new NoSuchElementException();
    }
}
