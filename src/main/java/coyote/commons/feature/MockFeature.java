package coyote.commons.feature;

public class MockFeature extends Feature {

  public MockFeature( String name, MenuIcon icon, MenuLocation location ) {
    setName( name );
    setIcon( icon );
    addLocation( location );
  }

}
