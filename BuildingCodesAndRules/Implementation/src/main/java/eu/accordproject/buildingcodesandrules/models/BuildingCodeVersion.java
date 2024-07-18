/*
Tom Beach
Copyright (c) Cardiff University
*/

package eu.accordproject.buildingcodesandrules.models;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuildingCodeVersion {
    @Getter
    @Setter
    private String version;
    
    @Getter
    @Setter
    @JsonFormat(pattern = "YYYY-MM-dd")
    private LocalDate versionDate;
}