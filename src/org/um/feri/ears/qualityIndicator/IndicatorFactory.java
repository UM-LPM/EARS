package org.um.feri.ears.qualityIndicator;
import org.um.feri.ears.problems.moo.MOProblemBase;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;

public final class IndicatorFactory<T> {
	
	private IndicatorFactory(){}
	
    public static <T> QualityIndicator<T> createIndicator(IndicatorName name, MOProblemBase moProblemBase) {
    	QualityIndicator<T> qi = null;
        switch (name) {
        case CovergeOfTwoSets:
        	qi = new CoverageOfTwoSets<T>(moProblemBase);
            break;
 
        case Epsilon:
            qi = new Epsilon<T>(moProblemBase);
            break;
            
        case EpsilonBin:
            qi = new EpsilonBin<T>(moProblemBase);
            break;    

        case ErrorRatio:
            qi = new ErrorRatio<T>(moProblemBase);
            break;
            
        case GD:
            qi = new GenerationalDistance<T>(moProblemBase);
            break;
            
        case Hypervolume:
            qi = new Hypervolume<T>(moProblemBase);
            break;
            
        case IGD:
            qi = new InvertedGenerationalDistance<T>(moProblemBase);
            break;
            
        case IGDPlus:
            qi = new InvertedGenerationalDistancePlus<T>(moProblemBase);
            break;
            
        case MPFE:
            qi = new MaximumParetoFrontError<T>(moProblemBase);
            break;
            
        case MaximumSpread:
            qi = new MaximumSpread<T>(moProblemBase);
            break;
        
        case NativeHV:
            qi = new NativeHV<T>(moProblemBase);
            break;
            
        case NR:
            qi = new NR<T>(moProblemBase);
            break;
            
        case ONVG:
            qi = new OverallNondominatedVectorGeneration<T>(moProblemBase);
            break;
            
        case ONVGR:
            qi = new OverallNondominatedVectorGenerationRatio<T>(moProblemBase);
            break;
            
        case R1:
            qi = new R1<T>(moProblemBase);
            break;
            
        case R2:
            qi = new R2<T>(moProblemBase);
            break;
            
        case R3:
            qi = new R3<T>(moProblemBase);
            break;
            
        case RNI:
            qi = new RatioOfNondominatedIndividuals<T>(moProblemBase);
            break;
            
        case Spacing:
            qi = new Spacing<T>(moProblemBase);
            break;
            
        case Spread:
            qi = new Spread<T>(moProblemBase);
            break;
        case GeneralizedSpread:
        	qi = new GeneralizedSpread<T>(moProblemBase);
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