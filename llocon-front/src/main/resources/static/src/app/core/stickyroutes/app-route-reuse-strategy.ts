import { ActivatedRouteSnapshot, RouteReuseStrategy, DetachedRouteHandle } from '@angular/router';

/**
 * Interface for object which can store both: 
 *     - An ActivatedRouteSnapshot, which is useful for determining whether or not you should attach a route (see this.shouldAttach).
 *     - A DetachedRouteHandle, which is offered up by this.retrieve, in the case that you do want to attach the stored route.
 */
export class AppRouteReuseStrategy implements RouteReuseStrategy {

    storedRouteHandles = new Map<string, DetachedRouteHandle>();

    // Decides if the route should be stored
    shouldDetach( route: ActivatedRouteSnapshot ): boolean {
        //console.log('>>> shouldDetach', route, this.getUrlFromRoute(route))
        if ( route.data && this.getKeyFromRoute( route ) ) {
            return true;
        } else {
            return false;
        }
    }
    // Store the information for the route we're destructing
    store( route: ActivatedRouteSnapshot, handle: DetachedRouteHandle ): void {
        this.storedRouteHandles.set( this.getKeyFromRoute( route ), handle );
    }

    // Return true if we have a stored route object for the next route
    shouldAttach( route: ActivatedRouteSnapshot ): boolean {
        return this.storedRouteHandles.has( this.getKeyFromRoute( route ) );
    }

    // If we returned true in shouldAttach(), now return the actual route data for restoration
    retrieve( route: ActivatedRouteSnapshot ): DetachedRouteHandle {
        return this.storedRouteHandles.get( this.getKeyFromRoute( route ) );
    }

    // Reuse the route if we're going to and from the same route
    shouldReuseRoute( future: ActivatedRouteSnapshot, curr: ActivatedRouteSnapshot ): boolean {
        return future.routeConfig === curr.routeConfig;
    }

    getKeyFromRoute( route: ActivatedRouteSnapshot ) {
        return route.data["stickyKey"];
    }

    getUrlFromRoute( route: ActivatedRouteSnapshot ) {
        let url = route.url.join("/")
        let currentRoute = route;
        while (currentRoute.parent && currentRoute.parent.url.length) {
            url += "/" + currentRoute.url.join("/")
            currentRoute = route.parent;
        }
        return url;
    }

}