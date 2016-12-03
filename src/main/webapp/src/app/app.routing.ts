import  {ModuleWithProviders} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {LogReportComponent} from "./logreport/logreport.component";
import {ReplayComponent} from "./replay/replay.component";

const appRoutes: Routes = [
    {
        path: '',
        redirectTo: '/logreport',
        pathMatch: 'full'
    },
    {
        path: 'logreport',
        component: LogReportComponent
    },
    {
        path: 'replay',
        component: ReplayComponent
    }
];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);