package com.gestvicole.gestionavicole.wrapper;

import com.gestvicole.gestionavicole.entities.Building;
import com.gestvicole.gestionavicole.entities.LayerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuildingSaveEntity {
    private Building building;
    private List<LayerType> layerTypes;
}
