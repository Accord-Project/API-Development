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
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuildingCodeIndex {
    
    @Getter
    @Setter
    private String shortName;
    
    @Getter
    @Setter
    private String fullName;

    @Getter
    @Setter
    private String latestVersion;
    
    @Getter
    @Setter
    @JsonFormat(pattern = "YYYY-MM-dd")
    private LocalDate latestVersionDate;

    @Getter
    @Setter
    private String language;

    @Getter
    @Setter
    private String jurisdiction;

    @Getter
    @Builder.Default
    private List<BuildingCodeVersion> versions = new ArrayList<BuildingCodeVersion>();

}
