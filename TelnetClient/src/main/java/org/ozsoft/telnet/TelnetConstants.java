package org.ozsoft.telnet;

/**
 * Constants used by the Telnet protocol.
 */
public interface TelnetConstants {
    
    // Terminal characters.
    int NUL     = 0;
    int BEL     = 7;
    int BS      = 8;
    int HT      = 9;
    int LF      = 10;
    int VT      = 11;
    int FF      = 12;
    int CR      = 13;
    
    // Commands.
    int SE      = 240;
    int NOP     = 241;
    int DM      = 242;
    int BRK     = 243;
    int IP      = 244;
    int AO      = 245;
    int AYT     = 246;
    int EC      = 247;
    int EL      = 248;
    int GA      = 249;
    int SB      = 250;
    int WILL    = 251;
    int WONT    = 252;
    int DO      = 253;
    int DONT    = 254;
    int IAC     = 255;
    
    // Options.
    int ECHO                    = 1;
    int SUPPRESS_GA             = 3;
    int STATUS                  = 5;
    int TIMING_MARK             = 6;
    int TERM_TYPE               = 24;
    int WINDOW_SIZE             = 31;
    int TERM_SPEED              = 32;
    int REMOTE_FLOW_CTRL        = 33;
    int LINE_MODE               = 34;
    int ENV_VARIABLES           = 36;
    
}
