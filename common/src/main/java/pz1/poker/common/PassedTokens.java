package pz1.poker.common;

import lombok.*;

/**Class representing Tokens Passed between Client and Server.**/
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PassedTokens {
    /**ID of Player that is sending/receiving tokens.**/
    private Object playerID;
    /**Type of Action.**/
    private ActionType actionType;
    /**Parameters of action such as amount of bet coins with Action 'BET'.**/
    private Object actionParameters;
}
