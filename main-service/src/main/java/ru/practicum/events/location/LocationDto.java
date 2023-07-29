package ru.practicum.events.location;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDto {
    private float lat;
    private float lon;
}
