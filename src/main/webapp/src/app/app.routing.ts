import  {ModuleWithProviders} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {LogReportComponent} from "./logreport/logreport.component";
import {ReplayComponent} from "./replay/replay.component";
import {UsageComponent} from "./usage/usage.component";
import {PropsComponent} from "./props/props.component";

const appRoutes: Routes = [
    {
        path: '',
        redirectTo: '/props',
        pathMatch: 'full'
    },
    {
        path: 'logreport',
        component: LogReportComponent
    },
    {
        path: 'replay',
        component: ReplayComponent
    },
    {
        path: 'usage',
        component: UsageComponent
    },
    {
        path: 'props',
        component: PropsComponent
    }
];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);