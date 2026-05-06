import { Pipe, PipeTransform } from '@angular/core';

// Extracts "01" from "L2-01" → shows just the slot number
@Pipe({ name: 'fpSlotNum' })
export class FloorPlanSlotNumPipe implements PipeTransform {
  transform(slotNumber: string): string {
    const match = slotNumber.match(/-(\d+)$/);
    return match ? match[1] : slotNumber;
  }
}

// Count available slots in an array
@Pipe({ name: 'availableCount' })
export class AvailableCountPipe implements PipeTransform {
  transform(slots: any[]): number {
    return slots ? slots.filter(s => s.available).length : 0;
  }
}
