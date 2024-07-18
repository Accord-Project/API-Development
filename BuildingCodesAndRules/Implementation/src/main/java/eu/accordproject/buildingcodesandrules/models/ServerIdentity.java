/*
Tom Beach
Copyright (c) Cardiff University
*/

package eu.accordproject.buildingcodesandrules.models;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServerIdentity {
    @Getter
    @Setter
    private String name;
    
    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String operator;

    @Getter
    @Builder.Default
    private List<BuildingCodeIndex> documentList = new ArrayList<BuildingCodeIndex>();

}
