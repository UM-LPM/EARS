package org.um.feri.ears.quality_indicator;
import org.um.feri.ears.quality_indicator.QualityIndicator.IndicatorName;

public final class IndicatorFactory<T> {
	
	private IndicatorFactory(){}
	
    public static <T extends Number> QualityIndicator<T> createIndicator(IndicatorName name, int num_obj, String file_name) {
    	QualityIndicator<T> qi = null;
        switch (name) {
        case COVERAGE_OF_TWO_SETS:
        	qi = new CoverageOfTwoSets<T>(num_obj);
            break;
 
        case EPSILON:
            qi = new Epsilon<T>(num_obj, file_name);
            break;
            
        case EPSILON_BIN:
            qi = new EpsilonBin<T>(num_obj);
            break;    

        case ERROR_RATIO:
            qi = new ErrorRatio<T>(num_obj, file_name);
            break;
            
        case GD:
            qi = new GenerationalDistance<T>(num_obj, file_name);
            break;
            
        /*case Hypervolume:
            qi = new Hypervolume<T>(num_obj, file_name);
            break;*/
            
        case IGD:
            qi = new InvertedGenerationalDistance<T>(num_obj, file_name);
            break;
            
        case IGD_PLUS:
            qi = new InvertedGenerationalDistancePlus<T>(num_obj, file_name);
            break;
            
        case MPFE:
            qi = new MaximumParetoFrontError<T>(num_obj, file_name);
            break;
            
        case MAXIMUM_SPREAD:
            qi = new MaximumSpread<T>(num_obj, file_name);
            break;
        
        case NATIVE_HV:
            qi = new NativeHV<T>(num_obj, file_name);
            break;
            
        case NR:
            qi = new NR<T>(num_obj);
            break;
            
        case ONVG:
            qi = new OverallNondominatedVectorGeneration<T>(num_obj);
            break;
            
        case ONVGR:
            qi = new OverallNondominatedVectorGenerationRatio<T>(num_obj, file_name);
            break;
            
        case R1:
            qi = new R1<T>(num_obj, file_name);
            break;
            
        case R2:
            qi = new R2<T>(num_obj, file_name);
            break;
            
        case R3:
            qi = new R3<T>(num_obj, file_name);
            break;
            
        case RNI:
            qi = new RatioOfNondominatedIndividuals<T>(num_obj);
            break;
            
        case SPACING:
            qi = new Spacing<T>(num_obj, file_name);
            break;
            
        case SPREAD:
            qi = new Spread<T>(num_obj, file_name);
            break;
        case GENERALIZED_SPREAD:
        	qi = new GeneralizedSpread<T>(num_obj, file_name);
        	break;
        	
        /*case WFGHypervolume:
        	qi = new WFGHypervolume(problem);
        	break;*/
            
        default:
            qi = null;
            break;
        }
        return qi;
    }
}